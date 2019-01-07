rm(list = ls(all = T))
################################################################################
library(dplyr)
library(ggplot2)
library(wesanderson) #color palette for ggplot
################################################################################
ds.names <- list.files(path = "~/Dropbox/Swinburne/Github/codes/DiabetesDiscoveryV2/resources/R-utilities/report_datasets/315_datasets")
openml <- read.csv("extract-original-datasets-1.csv", header = TRUE)
ranking.evaluation.dsnames <- list.files(path = "~/Dropbox/Swinburne/Github/codes/DiabetesDiscoveryV2/resources/R-utilities/report_datasets/ranking_evaluation")

t <- readRDS("merged-2.rds")

openml$dsnames <- gsub(pattern = "https://www.openml.org/data/download/\\d+/", "",
                   x = openml$url)
openml$dsnames <- gsub(pattern = ".arff", "",
                       x = openml$dsnames)
openml$path <- paste0("https://openml2.win.tue.nl/d/", openml$data_id)

ds.names <- gsub(pattern = ".arff", "",
                 x = ds.names)
ranking.evaluation.dsnames <- gsub(pattern = ".arff", "",
                 x = ranking.evaluation.dsnames)

openml <- openml %>%
  group_by(dsnames) %>%
  mutate(row.number = 1:n()) %>%
  as.data.frame()

openml <- openml %>%
  group_by(dsnames) %>%
  filter(row.number == 1) %>%
  as.data.frame()

openml <- openml %>%
  filter(dsnames %in% ds.names) %>%
  as.data.frame()

# ds.names <- as.data.frame(ds.names)
# 
# ds.names <- ds.names %>%
#   filter(! (ds.names %in% openml$dsnames)) %>%
#   as.data.frame()

openml <- openml[c("name", "path", "dsnames")]
ranking.openml <- openml %>%
  filter(dsnames %in% ranking.evaluation.dsnames) %>%
  mutate(name = paste0("cellcolor[gray]{0.8}{", name, "}"),
         path = paste0("cellcolor[gray]{0.8}{", path, "}")) %>%
  as.data.frame()
nonranking.openml <- openml %>%
  filter(!(dsnames %in% ranking.evaluation.dsnames)) %>%
  as.data.frame()
export <- rbind(ranking.openml, nonranking.openml)
export <- export %>%
  arrange(dsnames) %>%
  as.data.frame()

xtable::xtable(export[c("name", "path")])


v <- c("Finance", "Healthcar", "Image", "Misc", "Biology", "Vehicle")
sort(v)

