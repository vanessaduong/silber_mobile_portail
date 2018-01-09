package esipe.fisa.silber.beans;

import android.text.format.DateFormat;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by zouhairhajji on 16/11/2017.
 */

public class BankStatement {

    private String path;

    private Long generationDate;

    private int idAccount;

    private String accountType;

    public BankStatement(String path, Long generationDate, int idAccount, String accountType) {
        this.path = path;
        this.generationDate = generationDate;
        this.idAccount = idAccount;
        this.accountType = accountType;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(Long generationDate) {
        this.generationDate = generationDate;
    }

    public int getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        Date genDate = new Date(getGenerationDate());
        String monthNumber  = (String) DateFormat.format("MM",   genDate);
        String year         = (String) DateFormat.format("yyyy", genDate);
        String accType = getAccountType();
        String title = accType + "-" + monthNumber + "/" + year;
        return title;
    }
}
