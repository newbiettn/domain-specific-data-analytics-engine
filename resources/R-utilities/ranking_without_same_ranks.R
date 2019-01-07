rm(list = ls(all = T))
################################################################################
# Load library
library(ConsRank)
################################################################################
# Ranking 
dt <- read.csv("ranking.csv", header = FALSE)
if (nrow(dt)>1){
  ranking <- QuickCons(dt)
  # Extract the ranking from the list result
  consensus <- ranking$Consensus
  last.row <- consensus[nrow(consensus),]
  last.row <- t(as.matrix(last.row))
  # Sort the list but still keep the workflow id 
  last.row <- last.row[,order(last.row, decreasing = T)]
} else {
  s <- dt[1,]
  last.row <- sort(s, decreasing = TRUE)
}

# Extract top 10 best, if they have the same rank, pick randomly
m <- max(last.row)
top.workflow <- c()
for (i in 0:9){
  current <- last.row[which(last.row == (m-i))]
  random.pick <- sample(1:length(current), 1)
  workflow.id <- names(current[random.pick])
  top.workflow <- c(top.workflow, workflow.id)
}

# Remove NA in case there are not enough 10 workflows
top.workflow <- as.vector(na.omit(top.workflow))
################################################################################
# Print the top 5 workflow ids into file to be read by Java
str <- paste(top.workflow, collapse = ",")
# Write to file
write(str, "best_workflow_index.txt")


