rm(list = ls(all = T))
################################################################################
# Load library
library(dplyr)
library(ggplot2)
library(reshape2)
library(wesanderson) #color palette for ggplot
################################################################################
# Load dataset
dt <- read.csv("evaluation_experiment_gmeans_auc.csv")

# dt <- read.csv("evaluation_experiment_kmeans_auc.csv")
dt$DATASET <- as.factor(dt$DATASET)
number.of.workflows <- 9
clustering.technique <- "GMeans"

# Summarise
dt <- dt %>%
  group_by(DATASET, RANK) %>%
  summarise(AUC = median(WEIGHTEDAREAUNDERROC)) %>%
  arrange(DATASET, RANK) %>%
  as.data.frame()
dt$RANK <- as.factor(dt$RANK)

summarize.by.rank <- function(rank, dt){
  tmp <- dt %>%
    group_by(DATASET) %>%
    filter(RANK == rank) %>%
    as.data.frame()
  v <- tmp[,c("AUC")]
  return(v)
}
performance.by.rank <- as.data.frame(sapply(1:number.of.workflows, summarize.by.rank, dt))
names(performance.by.rank) <- paste0("W", 1:ncol(performance.by.rank))
head(melt(performance.by.rank))

ggplot(melt(performance.by.rank)) +
  geom_boxplot(aes(x = variable, y = value)) +
  xlab("Workflow") +
  ylab("AUC") +
  ggtitle(paste0(number.of.workflows, " workflows", " - AUC (", clustering.technique, ")")) +
  theme_bw()
filename = paste0("evaluation_experiment-", clustering.technique, "_auc-direct_comparison_size_", number.of.workflows, ".png")
ggsave(filename)

################################################################################
# Create comparison windows
create.comparison.window.1 <- function(size, size.list, performance.by.rank){
  size.1 <- size.list[[size]]
  size.2 <- setdiff(1:number.of.workflows, size.1)
  window.1 <- as.data.frame(performance.by.rank[,size.1])
  window.2 <- as.data.frame(performance.by.rank[,size.2])
  print(window.1)
  print(window.2)
  best.of.window.1 <- apply(window.1, 1, max)
  best.of.window.2 <- apply(window.2, 1, max)
  fraction <- round((best.of.window.1/best.of.window.2)*100, digits = 4)
  return (fraction)
}
size.list <- sapply(1:number.of.workflows, function(x){c(1:number.of.workflows)[1:x]})
window.performance <- sapply(1:(number.of.workflows-1), create.comparison.window.1, size.list, performance.by.rank)
window.performance <- as.data.frame(window.performance)
names(window.performance) <- paste0("C", 1:ncol(window.performance))

ggplot(melt(window.performance)) +
  geom_boxplot(aes(y = value, x = variable)) + 
  xlab("Case") +
  ylab("AUC") +
  ggtitle("Performance of Workflows ") +
  theme_bw() +
  theme(legend.position="bottom", 
        legend.background = element_rect(fill="gray90", size=1, linetype="dotted")) +
  scale_fill_manual("Case", values=wes_palette(n=3, name="Moonrise3"))

ggplot(melt(window.performance)) +
  geom_point(aes(x = value, y = variable)) + 
  xlab("Case") +
  ylab("AUC") +
  ggtitle(paste0(number.of.workflows, " workflows", " - AUC (", clustering.technique, ")")) +
  theme_bw() +
  theme(legend.position="bottom", 
        legend.background = element_rect(fill="gray90", size=1, linetype="dotted")) +
  scale_fill_manual("Case", values=wes_palette(n=3, name="Moonrise3"))


hist(window.performance$C1)

window.performance.cat <- apply(window.performance, 2, 
                                function(x) cut(x, 
                                                breaks = c(0, 99, 100, 1000), 
                                                right = FALSE,
                                                labels = c("Low", "Normal", "High")))


head(melt(window.performance.cat))
t <- melt(window.performance.cat)
names(t) <- c("dataset", "window", "fraction")
t$window <- as.character(t$window)

levels(t$fraction)
t$fraction <- factor(t$fraction, levels(t$fraction)[c(2, 3, 1)])
t <- t[order(t$fraction),]

t <- t %>%
  group_by(window, fraction) %>%
  summarise(total.count=round(n()/32*100, digits = 2)) %>%
  mutate(label_y = cumsum(total.count)) %>%
  as.data.frame()

ggplot(t, aes(x = window, fill = fraction, y = total.count)) +
  geom_bar(stat = "identity") + 
  geom_text(aes(label = paste0(total.count, "%"), y = label_y, x = window),  
            vjust = 1.1, color = "white", size = 4.5) +
  xlab("Case") +
  ylab("Percentage") +
  ggtitle(paste0(number.of.workflows, " workflows", " - AUC (", clustering.technique, ")")) +
  theme_bw() +
  theme(legend.position="bottom", 
        legend.background = element_rect(fill="gray90", size=1, linetype="dotted")) +
  scale_fill_manual("Relative\nperformance", values=wes_palette(n=3, name="Moonrise3"))


filename = paste0("evaluation_experiment-", clustering.technique, "_auc-fraction_size_", number.of.workflows, ".png")
ggsave(filename)
################################################################################
create.comparison.window.2 <- function(size, size.list, performance.by.rank){
  size.1 <- size.list[[size]]
  size.2 <- setdiff(1:number.of.workflows, size.1)
  window.1 <- as.data.frame(performance.by.rank[,size.1])
  window.2 <- as.data.frame(performance.by.rank[,size.2])
  best.of.window.1 <- apply(window.1, 1, max)
  best.of.window.2 <- apply(window.2, 1, max)
  # fraction <- round((best.of.window.1/best.of.window.2), digits = 4)
  tmp <- data.frame(best.of.window.1, best.of.window.2)
  return (tmp)
}
size.list <- sapply(1:number.of.workflows, function(x){c(1:number.of.workflows)[1:x]})
tmp.dt <- data.frame()
for (i in 1:(number.of.workflows-1)){
  tmp <- create.comparison.window.2(i, size.list, performance.by.rank)
  case <- paste0("C", i)
  tmp.dt <- rbind(tmp.dt, cbind(tmp, case))
}

tmp.dt <- melt(tmp.dt)

ggplot(tmp.dt) +
  geom_boxplot(aes(y = value, x = case, fill = variable)) + 
  xlab("Case") +
  ylab("AUC") +
  ggtitle(paste0(number.of.workflows, " workflows", " - AUC (", clustering.technique, ")")) +
  theme_bw() +
  theme(legend.position="bottom", 
        legend.background = element_rect(fill="gray90", size=1, linetype="dotted")) +
  scale_fill_manual("Window", values=wes_palette(n=3, name="Moonrise3"),
                    labels = c("First", "Second"))

filename <-  paste0("evaluation_experiment-", clustering.technique, "_auc-windows_size_", number.of.workflows, ".png")
ggsave(filename)




