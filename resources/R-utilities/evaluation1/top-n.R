rm(list = ls(all = T))
################################################################################
library(dplyr)
library(FNN)
library(ConsRank)
library(latex2exp)
################################################################################
test.results <- read.csv("ranking_evaluation/test_dataset_results.csv", header = TRUE)
gmeans.top.n.ids <- read.csv("ranking_evaluation/top.n.gmeans.csv", header = FALSE)
kmeans.top.n.ids <- read.csv("ranking_evaluation/top.n.kmeans.csv", header = FALSE)
noclustering.top.n.ids <- read.csv("ranking_evaluation/top.n.noclustering.csv", header = FALSE)
base.mf <- read.csv("ranking_evaluation/mf_with_dsnames.csv", header = FALSE)
base.training <- read.csv("ranking_evaluation/base.training.with.ranking.csv", header = TRUE)
test.mf <- read.csv("ranking_evaluation/mf.test.set.csv", header = FALSE)
################################################################################
noclustering.top.n.ids <- data.frame(gmeans.top.n.ids$V1, -1, noclustering.top.n.ids)
colnames(noclustering.top.n.ids) <- paste0("V", 1:ncol(noclustering.top.n.ids))
################################################################################
# Base-line method
base.training$DATASET <- as.character(base.training$DATASET)
base.training <- base.training %>%
  mutate(ID = paste0(ATTRIBUTESELECTION, "&", CLASSIFIER)) %>%
  as.data.frame()
base.training$ID <- as.factor(base.training$ID)
base.training <- base.training %>%
  arrange(DATASET, ID) %>%
  as.data.frame()
 
knn.evaluate <- function(k){
  base.mf$id <- 1:nrow(base.mf)
  model <- knn(base.mf[2:20], test.mf[2:20], rep("dummyclass", nrow(base.mf)), k = k)
  indices <- attr(model, "nn.index")
  baseline.top.n.ids <- data.frame()
  for (v in 1:47){
    neighbor.dataset <- filter(base.mf, id %in% indices[v,])
    neighbor.dataset.names <- as.character(neighbor.dataset$V1)
    subset.base.training <- filter(base.training, DATASET %in% neighbor.dataset.names)
    rank <- data.frame()
    single.base.training <- data.frame()
    for (ds in neighbor.dataset.names){
      single.base.training <- subset.base.training %>%
        filter(DATASET == ds) %>%
        as.data.frame()
      rank <- rbind(rank, single.base.training$rank)
    }
    pred.rank <- apply(rank, 2, mean)
    pred.rank.dt <- data.frame(single.base.training$ID, pred.rank)
    pred.rank.dt <- pred.rank.dt %>%
      arrange(pred.rank)
    top.n.ids <- t(as.data.frame(pred.rank.dt$single.base.training.ID))
    baseline.top.n.ids <- rbind(baseline.top.n.ids, 
                                cbind(V1 = as.character(test.mf$V1[v]), -1, top.n.ids))
  }
  return(baseline.top.n.ids)
}
baseline.top.n.ids.with.k.1 <- knn.evaluate(1)
baseline.top.n.ids.with.k.2 <- knn.evaluate(2)
# baseline.top.n.ids.with.k.3 <- knn.evaluate(15)
baseline.top.n.ids.with.k.3 <- knn.evaluate(267)
################################################################################
top.n.evaluate <- function(dt, recommended, method.name){
  dt <- dt %>%
    mutate(ID = paste0(ATTRIBUTESELECTION, "&", CLASSIFIER)) %>%
    as.data.frame()
  
  dt <- dt %>%
    group_by(DATASET, ID) %>%
    summarize(MEANAUC = mean(WEIGHTEDAREAUNDERROC),
              MEANTIMEELAPSED = mean(TIMEELAPSED)) %>%
    as.data.frame()
  
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
  
  ds.names <- unique(as.character(dt$DATASET))
  avg.auc.of.top.n <- c()
  first.quartile.auc.of.top.n <- c()
  total.cost.of.top.n <- c()
  rbo.score <- c()
  for (n in seq(1, 100, 2)){
    max.auc.of.all.datasets <- c()
    min.auc.of.all.datasets <- c()
    cost.of.all.datasets <- 0
    rbo.of.all.datasets <- c()
    for (ds in ds.names){
      subset.recommended <- recommended %>%
        filter(V1 == ds) %>%
        as.data.frame()
      top.n.ids <- as.vector(t(subset.recommended[,3:(n+2)]))
      # print(top.n.ids)
      subset.dt <- dt %>%
        filter(ID %in% top.n.ids, DATASET == ds) %>%
        arrange(ID) %>%
        as.data.frame()
      
      rbo.tmp <- dt %>%
        filter(DATASET == ds) %>%
        arrange(rank) %>%
        as.data.frame()
      ground.truth.top.n.ids <- rbo.tmp$ID[1:n]
      rbo <- rbo_ext(ground.truth.top.n.ids, top.n.ids, p = 0.95)
      
      max.auc.per.dataset <- max(subset.dt$MEANAUC[1:n])
      min.auc.per.dataset <- min(subset.dt$MEANAUC[1:n])
      cost.per.dataset <- sum(subset.dt$MEANTIMEELAPSED[1:n])
      
      max.auc.of.all.datasets <- c(max.auc.of.all.datasets, max.auc.per.dataset)
      min.auc.of.all.datasets <- c(min.auc.of.all.datasets, max.auc.per.dataset)
      rbo.of.all.datasets <- c(rbo.of.all.datasets, rbo)
      cost.of.all.datasets <- cost.of.all.datasets + cost.per.dataset
    }
    avg.auc.of.top.n <- c(avg.auc.of.top.n, mean(max.auc.of.all.datasets))
    first.quartile.auc.of.top.n <- c(first.quartile.auc.of.top.n, quantile(max.auc.of.all.datasets, 0.25))
    rbo.score <- c(rbo.score, mean(rbo.of.all.datasets))
    total.cost.of.top.n <- c(total.cost.of.top.n, mean(cost.of.all.datasets))
  }
  k <- seq(1, 100, 2)
  auc.dt <- cbind(k, avg.auc.of.top.n)
  worst.dt <- cbind(k, first.quartile.auc.of.top.n)
  rbo.dt <- cbind(k, rbo.score)
  cost.dt <- cbind(k, total.cost.of.top.n)
  
  auc.dt <- data.frame(auc.dt)
  auc.dt$method.name <- method.name
  
  worst.dt <- data.frame(worst.dt, row.names = NULL)
  worst.dt$method.name <- method.name
  
  rbo.dt <- data.frame(rbo.dt)
  rbo.dt$method.name <- method.name
  
  cost.dt <- data.frame(cost.dt)
  cost.dt$method.name <- method.name
  return (list(auc.dt = auc.dt, 
               worst.dt = worst.dt,
               rbo.dt = rbo.dt,
               cost.dt = cost.dt))
}

gmeans.evaluation <- top.n.evaluate(test.results, gmeans.top.n.ids, "Mg")
kmeans.evaluation <- top.n.evaluate(test.results, kmeans.top.n.ids, "Mk")
base.line.evaluation.1 <- top.n.evaluate(test.results, baseline.top.n.ids.with.k.1, "1-nn")
base.line.evaluation.2 <- top.n.evaluate(test.results, baseline.top.n.ids.with.k.3, "N-nn")
base.line.evaluation.3 <- top.n.evaluate(test.results, noclustering.top.n.ids, "Df")
################################################################################
# Plot
auc.dt <- rbind(
                kmeans.evaluation$auc.dt,
                base.line.evaluation.1$auc.dt,
                base.line.evaluation.2$auc.dt,
                base.line.evaluation.3$auc.dt
                )
ggplot(auc.dt) +
  geom_point(aes(x = k, y = avg.auc.of.top.n, shape = method.name, colour = method.name))  +
  geom_path(aes(x = k, y = avg.auc.of.top.n, colour = method.name), size = 0.7) +
  xlab("top-n") +
  ylab(TeX("$\\bar{AUC}$")) +
  theme_linedraw() +
  theme(panel.grid.minor = element_blank()) +
  theme(legend.position = c(1, 0), 
        legend.justification = c(1, 0),
        legend.background = element_rect(colour = "black", size = 0.1),
        legend.title = element_blank(),
        text = element_text(size=16))

ggsave("figs/1st_evaluation_auc.png")

# rbo
rbo.dt <- rbind(
                  kmeans.evaluation$rbo.dt,
                  base.line.evaluation.1$rbo.dt,
                  base.line.evaluation.2$rbo.dt,
                base.line.evaluation.3$rbo.dt)
ggplot(rbo.dt) +
  geom_point(aes(x = k, y = rbo.score, shape = method.name, colour = method.name))  +
  geom_path(aes(x = k, y = rbo.score, colour = method.name), size = 0.7) +
  xlab("top-n") +
  ylab(TeX("$\\bar{RBO}$, (p = 0.95)")) +
  theme_linedraw() +
  theme(panel.grid.minor = element_blank()) +
  theme(legend.position = c(1, 0), 
        legend.justification = c(1, 0),
        legend.background = element_rect(colour = "black", size = 0.1),
        legend.title = element_blank(),
        text = element_text(size=16))
ggsave("figs/1st_evaluation_rbo.png")

# worst-case
worst.dt <- rbind(
                kmeans.evaluation$worst.dt,
                base.line.evaluation.1$worst.dt,
                base.line.evaluation.2$worst.dt,
                base.line.evaluation.3$worst.dt)
ggplot(worst.dt) +
  geom_point(aes(x = k, y = first.quartile.auc.of.top.n, shape = method.name, colour = method.name))  +
  geom_path(aes(x = k, y = first.quartile.auc.of.top.n, colour = method.name), size = 0.7) +
  xlab("top-n") +
  ylab("1st quantile AUC") +
  theme_linedraw() +
  theme(panel.grid.minor = element_blank()) +
  theme(legend.position = c(1, 0), 
        legend.justification = c(1, 0),
        legend.background = element_rect(colour = "black", size = 0.1),
        legend.title = element_blank(),
        text = element_text(size=16))
ggsave("figs/1st_evaluation_worst_case.png")

cost.dt <- rbind(
                kmeans.evaluation$cost.dt,
                base.line.evaluation.1$cost.dt,
                base.line.evaluation.2$cost.dt,
                base.line.evaluation.3$cost.dt)
cost.dt$accuracy <- auc.dt$avg.auc.of.top.n
cost.dt$total.cost.of.top.n <- cost.dt$total.cost.of.top.n/1000/60
ggplot(cost.dt) +
  geom_point(aes(x = k, y = total.cost.of.top.n, shape = method.name, colour = method.name))  +
  geom_path(aes(x = k, y = total.cost.of.top.n, colour = method.name), size = 0.7) +
  xlab("top-n") +
  ylab("Mean execution time (minutes)") +
  theme_linedraw() +
  theme(panel.grid.minor = element_blank()) +
  theme(legend.position = c(1, 0), 
        legend.justification = c(1, 0),
        legend.background = element_rect(colour = "black", size = 0.1),
        legend.title = element_blank(),
        text = element_text(size=16))
ggsave("figs/1st_evaluation_cost.png")
################################################################################
extract.data <- function(dt, recommended, method.name){
  dt <- dt %>%
    mutate(ID = paste0(ATTRIBUTESELECTION, "&", CLASSIFIER)) %>%
    as.data.frame()
  
  dt <- dt %>%
    group_by(DATASET, ID) %>%
    summarize(MEANAUC = mean(WEIGHTEDAREAUNDERROC),
              MEANTIMEELAPSED = mean(TIMEELAPSED)) %>%
    as.data.frame()
  
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
  
  ds.names <- unique(as.character(dt$DATASET))
  avg.auc.of.top.n <- c()
  first.quartile.auc.of.top.n <- c()
  total.cost.of.top.n <- c()
  rbo.score <- c()
  n <- 10
  # for (n in seq(1, 100, 2)){
    max.auc.of.all.datasets <- c()
    min.auc.of.all.datasets <- c()
    cost.of.all.datasets <- 0
    rbo.of.all.datasets <- c()
    for (ds in ds.names){
      subset.recommended <- recommended %>%
        filter(V1 == ds) %>%
        as.data.frame()
      top.n.ids <- as.vector(t(subset.recommended[,3:(n+2)]))
      # print(top.n.ids)
      subset.dt <- dt %>%
        filter(ID %in% top.n.ids, DATASET == ds) %>%
        arrange(ID) %>%
        as.data.frame()
      
      rbo.tmp <- dt %>%
        filter(DATASET == ds) %>%
        arrange(rank) %>%
        as.data.frame()
      ground.truth.top.n.ids <- rbo.tmp$ID[1:n]
      rbo <- rbo_ext(ground.truth.top.n.ids, top.n.ids, p = 0.95)
      
      max.auc.per.dataset <- max(subset.dt$MEANAUC[1:n])
      min.auc.per.dataset <- min(subset.dt$MEANAUC[1:n])
      cost.per.dataset <- sum(subset.dt$MEANTIMEELAPSED[1:n])
      
      max.auc.of.all.datasets <- c(max.auc.of.all.datasets, max.auc.per.dataset)
      min.auc.of.all.datasets <- c(min.auc.of.all.datasets, max.auc.per.dataset)
      rbo.of.all.datasets <- c(rbo.of.all.datasets, rbo)
      cost.of.all.datasets <- cost.of.all.datasets + cost.per.dataset
    }
    auc.of.top.n <- max.auc.of.all.datasets
    # first.quartile.auc.of.top.n <- c(first.quartile.auc.of.top.n, quantile(max.auc.of.all.datasets, 0.25))
    rbo.score <- rbo.of.all.datasets
    total.cost.of.top.n <- cost.of.all.datasets
  # }
    
  auc.dt <- data.frame(auc.of.top.n)
  auc.dt$method.name <- method.name
  
  rbo.dt <- data.frame(rbo.score)
  rbo.dt$method.name <- method.name
  
  cost.dt <- data.frame(total.cost.of.top.n)
  cost.dt$method.name <- method.name
  return (list(auc.dt = auc.dt, 
               rbo.dt = rbo.dt,
               cost.dt = cost.dt))
}
gmeans.evaluation <- extract.data(test.results, gmeans.top.n.ids, "Mg")
kmeans.evaluation <- extract.data(test.results, kmeans.top.n.ids, "Mk")
base.line.evaluation.1 <- extract.data(test.results, baseline.top.n.ids.with.k.3, "1-nn")
base.line.evaluation.2 <- extract.data(test.results, noclustering.top.n.ids, "Df")

gmeans.auc <- gmeans.evaluation$auc.dt$auc.of.top.n
baseline1.auc <- base.line.evaluation.2$auc.dt$auc.of.top.n
t <- data.frame(gmeans.auc, baseline1.auc)
dt <- data.frame(gmeans = gmeans.auc>=baseline1.auc, kmeans = gmeans.auc <= baseline1.auc)
tbl <- table(dt)
mcnemar.test(tbl, correct = F)