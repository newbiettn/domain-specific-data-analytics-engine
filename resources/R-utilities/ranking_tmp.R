################################################################################
# Load library
library(ConsRank)
################################################################################
ds.cluster <- read.csv("dataset_cluster.csv")


################################################################################
# Ranking 
dt <- read.csv("ranking.csv", header = FALSE)

ranking <- QuickCons(dt)

# Extract the ranking from the list result
consensus <- ranking$Consensus
last.row <- consensus[nrow(consensus),]
last.row <- t(as.matrix(last.row))

# Sort the list but still keep the workflow id 
last.row <- last.row[,order(last.row, decreasing = T)]

# Extract the top 5 workflow id
m <- max(last.row)
top.workflow <- c()
for (i in 0:4){
  workflow.id <- names(last.row[which(last.row == (m-i))][1])
  top.workflow <- c(top.workflow, workflow.id)
}

# Print the top 5 workflow ids into file to be read by Java
str <- paste(top.workflow, collapse = ",")
write(str, "best_workflow_index.txt")

