package controllers;

import com.victorlaerte.asynctask.AsyncTask;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-05-22
 */
public class MyAsyncTask extends AsyncTask<String, Integer, Boolean> {
    @Override
    public void onPreExecute() {
        System.out.println("Background Thread will start");
    }

    @Override
    public Boolean doInBackground(String... params) {
        System.out.println("Background Thread is running");

        int i = 0;
        while (i < 5) {
            progressCallback(i);
            i++;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    @Override
    public void onPostExecute(Boolean success) {
        System.out.println("Background Thread has stopped");

        if (success) {
            System.out.println("Done with success");
        } else {
            System.out.println("Done with error");
        }
    }

    @Override
    public void progressCallback(Integer... params) {
        System.out.println("Progress " + params[0]);
    }
}
