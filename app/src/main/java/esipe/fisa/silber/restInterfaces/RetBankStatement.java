package esipe.fisa.silber.restInterfaces;

import java.util.List;

import esipe.fisa.silber.beans.BankStatement;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

/**
 * Created by zouhairhajji on 16/11/2017.
 */

public interface RetBankStatement {

    @GET("/bankstatement/all")
    Call<List<BankStatement>> getAllBankStatements();


    @GET("/bankstatement")
    Call<List<BankStatement>> getAllBankStatements(@Query("accountID") String accountID);

    @GET("/bankstatement/to")
    @Streaming
    Call<ResponseBody> getPDFFormat(@Query("bankStatementID") Integer bankStatementID);

}
