dt.1 <- read.csv("results_backup/autoweka_evaluation_result_errorrate_8000Mb_10threads_15m_seed_1.csv")
dt.2 <- read.csv("results_backup/autoweka_evaluation_result_errorrate_8000Mb_10threads_15m_seed_50.csv")
dt.3 <- read.csv("results_backup/autoweka_evaluation_result_errorrate_8000Mb_10threads_15m_seed_100.csv")
dt.4 <- read.csv("results_backup/autoweka_evaluation_result_errorrate_8000Mb_10threads_15m_seed_150.csv")
dt <- rbind(dt.1, dt.2, dt.3, dt.4)
dt <- dt %>%
  arrange(dataset, seed) %>%
  as.data.frame()
write.csv(dt, "results_backup/autoweka_evaluation_result_errorrate_8000Mb_10threads_15m_seed_1_150.csv",
          quote = FALSE,
          row.names = FALSE)

