################################################################################
normalize <- function(x) {
  min <- min(x, na.rm = T)
  max <- max(x, na.rm = T)
  (x - min) /(max - min)
}
################################################################################
mf <- read.csv("mf.csv", header = FALSE)
ncol <- ncol(mf)
stat <- data.frame()
for (i in 1:ncol){
  x <- mf[,i]
  min <- min(x, na.rm = T)
  max <- max(x, na.rm = T)
  stat <- rbind(stat, c(min, max))
  mf[,i] <- normalize(x)
}
colnames(stat) <- c("min", "max")
################################################################################
write.table(stat, "normalization.stat.csv", row.names = FALSE, col.names = FALSE, sep = ",")
write.table(mf, "normalized.mf.csv", row.names = FALSE, col.names = FALSE, sep = ",")











