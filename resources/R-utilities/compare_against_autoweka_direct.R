rm(list = ls(all = T))
################################################################################
library(dplyr)
library(ggplot2)
library(wesanderson) #color palette for ggplot
################################################################################
# dt.1 <- read.csv("myengine_evaluation_result-errorrate-kmeans_same_ranks-without_optimization.csv")
dt.1 <- read.csv("new_results_backup/myengine_evaluation_result-auc-gmeans.csv")
dt.2 <- read.csv("results_backup/autoweka_evaluation_result_auc_8000Mb_10threads_2m.csv")
# dt.2 <- read.csv("results_backup/autoweka_evaluation_result_errorrate_8000Mb_10threads_2m.csv")
# dt.2 <- read.csv("results_backup/autoweka_evaluation_result_errorrate_8000Mb_10threads_15m_seed_1_150.csv")

dt.2$weightedAreaUnderROC <- as.numeric(as.character(dt.2$weightedAreaUnderROC))
# Summarise
dt.1 <- dt.1 %>%
  group_by(dataset) %>%
  summarise(errorRate = median(weightedAreaUnderROC)) %>%
  arrange(dataset) %>%
  as.data.frame()

dt.2 <- dt.2 %>%
  group_by(dataset) %>%
  summarise(errorRate = median(weightedAreaUnderROC)) %>%
  arrange(dataset) %>%
  as.data.frame()


dt.1$engine <- "System (GMeans)"
dt.2$engine <- "AW (15-min)"

dt <- dt.1 %>%
  inner_join(dt.2, by = "dataset") %>%
  as.data.frame()

sum(dt$timeElapsed.x)
sum(dt$timeElapsed.y)

ggplot(dt) +
  geom_point(aes(x = errorRate.x, y = errorRate.y)) +
  geom_abline(slope=1) +
  theme_bw() +
  theme(legend.position="bottom", 
        legend.background = element_rect(fill="gray90", size=1, linetype="dotted")) +
  xlab("Error Rate (GMeans)") +
  ylab("Error Rate (Auto-Weka 15-m)") +
  scale_x_continuous(limits = c(0, 1)) +
  scale_y_continuous(limits = c(0, 1))
# ggsave("figs/direct_comparison_experiment_gmeans_vs_autoweka_15m.png")

ggplot(dt) +
  geom_point(aes(x = time, y = timeElapsed.y)) +
  geom_abline(slope=1) +
  theme_bw() +
  theme(legend.position="bottom", 
        legend.background = element_rect(fill="gray90", size=1, linetype="dotted")) +
  scale_x_continuous(limits = c(0, 200000)) +
  scale_y_continuous(limits = c(0, 200000))


