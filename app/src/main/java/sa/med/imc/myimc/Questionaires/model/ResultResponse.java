package sa.med.imc.myimc.Questionaires.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ResultResponse {

    @SerializedName("score")
    Score score;

    @SerializedName("message")
    String message;

    @SerializedName("status")
    String status;


    public void setScore(Score score) {
        this.score = score;
    }
    public Score getScore() {
        return score;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }


    public class Score {

        @SerializedName("sError")
        String sError;

        @SerializedName("stdError")
        String stdError;

        @SerializedName("T-Score")
        String TScore;

        @SerializedName("theta")
        String theta;


        public void setSError(String sError) {
            this.sError = sError;
        }
        public String getSError() {
            return sError;
        }

        public void setStdError(String stdError) {
            this.stdError = stdError;
        }
        public String getStdError() {
            return stdError;
        }

        public void setTScore(String TScore) {
            this.TScore = TScore;
        }
        public String getTScore() {
            return TScore;
        }

        public void setTheta(String theta) {
            this.theta = theta;
        }
        public String getTheta() {
            return theta;
        }

    }
}
