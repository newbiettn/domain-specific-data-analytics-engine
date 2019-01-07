rm(list = ls(all = T))
################################################################################
# library(gespeR)
library(dplyr)
library(ggplot2)
library(reshape2)
library(rbo)
library(wesanderson)

################################################################################
dt1 <- read.csv("ranking_dataset/evaluation_experiment_ranking_gmeans.csv", header = TRUE)
dt2 <- read.csv("ranking_dataset/evaluation_experiment_ranking_kmeans.csv", header = TRUE)
dt3 <- read.csv("ranking_dataset/evaluation_experiment_no_ranking.csv", header = TRUE)
recommmended1 <- read.csv("ranking_dataset/recommended_wk_gmeans.csv", header = FALSE)
recommmended2 <- read.csv("ranking_dataset/recommended_wk_kmeans.csv", header = FALSE)
recommmended3 <- read.csv("ranking_dataset/recommended_wk_noclustering.csv", header = FALSE)
################################################################################
# dt <- dt1
# recommmended <- recommmended1
compute_rbo <- function(dt, recommmended){
  dt <- dt %>%
    group_by(DATASET, FLOW) %>%
    summarize(MEANAUC = mean(WEIGHTEDAREAUNDERROC),
           MEANTIMEELAPSED = mean(TIMEELAPSED)) %>%
    as.data.frame()
  
  # Generate ID
  tmp.dt <- dt %>%
    group_by(DATASET) %>%
    count(DATASET) %>%
    as.data.frame()
  
  wk.id <- c()
  for (length in tmp.dt$n){
    l <- length
    s <- c(1:l)
    wk.id <- c(wk.id, s)
  }
  
  dt$ID <- wk.id
  
  
  dt <- dt %>%
    group_by(DATASET) %>%
    arrange(DATASET, desc(MEANAUC)) %>%
    mutate(rankByAUC = rank(-MEANAUC, ties.method = "min")) %>%
    as.data.frame()
  
  dt <- dt %>%
    group_by(DATASET, rankByAUC) %>%
    arrange(DATASET, rankByAUC, MEANTIMEELAPSED) %>%
    mutate(rankByTime = rank(MEANTIMEELAPSED, ties.method = "random")) %>%
    as.data.frame()
  
  myrank <- function(rankByErr, rankByTime){
    newrank <- c()
    if (length(unique(rankByErr)) == 1){
      newrank <- rankByErr + rankByTime - 1
    } else {
      newrank <- rankByErr
    }
    return (newrank)
  }
  dt <- dt %>%
    group_by(DATASET, rankByAUC, rankByTime) %>%
    arrange(DATASET, rankByAUC, rankByTime) %>%
    mutate(rank = myrank(rankByAUC, rankByTime)) %>%
    as.data.frame()
  ################################################################################
  ds.names <- unique(as.character(dt$DATASET))
  score.list <- list()
  k <- 1
  for (ds in c("car.arff", "cpu.arff", "dataset_23_cmc.arff",
               "dataset_28_optdigits.arff", "dataset_39_ecoli.arff",
               "dataset_48_tae.arff", "dataset_50_tic-tac-toe.arff",
               "phpGuu4iR.arff", "pollen.arff", "analcatdata_bankruptcy.arff",
               "analcatdata_halloffame.arff", "dataset_190_braziltourism.arff",
               "mbagrade.arff", "bodyfat.arff",
               "dataset_30_page-blocks.arff", "churn.arff", "allbp.arff",
               "dataset_28_optdigits.arff", "dataset_23_cmc.arff",
               "dataset_186_satimage.arff")){
    subset.dt <- dt %>%
      filter(DATASET == ds) %>%
      as.data.frame()
    subset.recommended <- recommmended %>%
      filter(V1 == ds) %>%
      as.data.frame()
    score <- c()
    for (j in seq(0, 100, 2)){
      list.wk1 <- as.character(subset.dt$ID[1:j])
      list.wk2 <- as.character(subset.recommended[1, 3:(j+2)])
      # names(list.wk1) <- as.character(subset.dt$ID[1:j])
      # names(list.wk2) <- as.character(subset.recommended[1, 3:(j+2)])
      print("============================")                           
      print("============================")
      print(list.wk1)
      print("============================")
      print(list.wk2)
      print("============================")
      print("============================")
      s <- rbo_ext(list.wk1, list.wk2, p = 0.9)
      score <- c(score, s)
    }
    score.list[[k]] <- score
    k <- k + 1
  }
  
  plot.dt <- data.frame(matrix(unlist(score.list), nrow = 20, byrow = T))
  median.plot.dt <- apply(plot.dt, 2, median)
  median.plot.dt <- melt(median.plot.dt)
  k <- seq(0, 100, 2)
  # print(plot.dt)
  median.plot.dt <- cbind(k, median.plot.dt)
  
  return (median.plot.dt)
}

################################################################################
# Plot
median.plot.dt.1 <- compute_rbo(dt1, recommmended1)
median.plot.dt.2 <- compute_rbo(dt2, recommmended2)
median.plot.dt.3 <- compute_rbo(dt3, recommmended3)

median.plot.dt <- data.frame(k = median.plot.dt.1$k, 
                             MG = median.plot.dt.1$value,
                             MK = median.plot.dt.2$value,
                             BL = median.plot.dt.3$value)
median.plot.dt <- melt(median.plot.dt, id = c("k"))
ggplot(median.plot.dt) +
  geom_point(aes(x = k, y = value, shape = variable, colour = variable))  +
  geom_path(aes(x = k, y = value, colour = variable)) +
  xlab("top-n") +
  ylab("RBO (p = 0.9)") +
  theme_linedraw() +
  theme(panel.grid.minor = element_blank()) +
  theme(legend.position = c(1, 0), 
       legend.justification = c(1, 0),
       legend.background = element_rect(colour = "black", size = 0.1),
       legend.title = element_blank(),
       text = element_text(size=16))

ggsave("figs/rbo_comparison.png")
################################################################################
a <- c(1, 2, 3)
b <- c(1, 8, 4)
# names(a) <- c(1, 2, 3, 5)
# names(b) <- c(1, 2, 3, 55)
rbo(a, b, p = 0.9, uneven.lengths = FALSE)
rbo_ext(a, b, p = 0.9)



substr(names(sort(subset.recommended[1, 2:(+1)])), 2, 1000)

w_rbo <- function(p, d){
  i <- 1:(d-1)
  w <-  1 - p^(d - 1) + ((1 - p) / p) * d * (log(1 / (1 - p)) - sum(p^i / i))
  return(w)
}
w_rbo(0.95, 20)

p = 0.9
i <- 1:5
sum(p^i / i)
ds.names <- unique(as.character(dt1$DATASET))