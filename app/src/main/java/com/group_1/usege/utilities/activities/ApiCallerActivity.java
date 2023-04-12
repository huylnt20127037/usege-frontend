package com.group_1.usege.utilities.activities;

import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import com.group_1.usege.R;
import com.group_1.usege.utilities.dto.ErrorResponse;
import com.group_1.usege.utilities.parser.JsonParser;
import com.group_1.usege.utilities.view.BusyHandingProgressManager;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import okhttp3.ResponseBody;
import retrofit2.Response;

public abstract class ApiCallerActivity<S> extends AppCompatActivity {

    public ApiCallerActivity(@LayoutRes int contentLayoutId)
    {
        super(contentLayoutId);
    }
    @Inject
    public BusyHandingProgressManager busyHandingProgressManager;
    @Inject
    public JsonParser jsonParser;

    /**
     * Inherit this method to show a custom message when failing to call an endpoint
     */
    protected void setCallApiFail()
    {
        Toast.makeText(this, R.string.error_connection, Toast.LENGTH_LONG).show();
    }

    /**
     * Call this method to mark starting calling an endpoint and show {@link com.group_1.usege.utilities.view.BusyHandlingProgressFragment}
     * @param provider provide the single response object from service class
     */
    protected final void startCallApi(Single<Response<S>> provider)
    {
        busyHandingProgressManager.show(getSupportFragmentManager().beginTransaction());
        provider
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleAfterCall);
    }

    /**
     * Call this method to mark starting calling an endpoint but not showing waiting load view {@link com.group_1.usege.utilities.view.BusyHandlingProgressFragment} and returning the response
     * @param provider provide the single response object from service class
     */
    protected final void startCallApiSilent(Single<Response<S>> provider)
    {
        provider
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleAfterCallSilent);
    }

    /**
     * Inherit this method to make more things when completing calling an endpoint (the base just dismiss the loading view {@link com.group_1.usege.utilities.view.BusyHandlingProgressFragment} in root)
     */
    protected void endCallApi()
    {
        busyHandingProgressManager.hide();
    }

    /**
     * Inherit this method to get the response after calling api
     * @param body the response body
     */
    protected abstract void handleCallSuccess(S body);
    /**
     * Inherit this method to get the fail response after calling api
     * @param errorResponse the error response
     */
    protected abstract void handleCallFail(ErrorResponse errorResponse);

    /**

     * <pre>
     * Registry this method to RxJava when calling an endpoint
     * .eg
     * accountServiceGenerator
     *      .getService()
     *      .confirm(username, code)
     *      .observeOn(AndroidSchedulers.mainThread())
     *      <b>.subscribe(this::handleAfterCall);</b>
     * </pre>
     * @param response response
     * @param throwable exception
     */
    private void handleAfterCall(Response<S> response, Throwable throwable) {
        if (throwable != null)
            setCallApiFail();
        else {
            if (response.isSuccessful())
                handleCallSuccess(response.body());
            else
                handleCallFail(response);
        }
        endCallApi();
    }

    private void handleAfterCallSilent(Response<S> response, Throwable throwable) {
        if (throwable != null)
            setCallApiFail();
        else if (!response.isSuccessful())
            handleCallFail(response);
    }


    /**
     * Please do not use this method, use the {@link #handleCallFail(ErrorResponse)} instead
     */
    private void handleCallFail(Response<S> response)
    {
        try (ResponseBody errorBody = response.errorBody()) {
            ErrorResponse errorResponse = jsonParser.fromJson(errorBody.charStream(), ErrorResponse.class);
            if (errorResponse == null || errorResponse.getMessage() == null)
                setCallApiFail();
            else
                handleCallFail(errorResponse);
        }
    }
}