################################################################################
stat <- read.csv("normalization.stat.csv", header = FALSE)
new.item <- read.csv("new.item.csv", header = FALSE)
################################################################################
normalize.test.set <- function(dt, stat){
  for (i in 1:ncol(dt)){
    x <- dt[,i]
    min <- stat[i, 1]
    max <- stat[i, 2]
    dt[,i] <- (x - min)/(max - min) 
  }
  return(dt)
}
normalized.new.item <- normalize.test.set(new.item, stat)
write.table(normalized.new.item, "normalized.new.item.csv",
          row.names = FALSE,
          col.names = FALSE,
          sep = ",")


