package binaryworksindonesia.com.gpstracker.api.request;


import binaryworksindonesia.com.gpstracker.api.APIService;
import binaryworksindonesia.com.gpstracker.api.ApiClient;
import binaryworksindonesia.com.gpstracker.api.response.HajjPerson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Meli Oktavia on 6/27/2016.
 */
public class HajjTrackerRequest {
    private ApiClient apiClient;
    private Call<HajjPerson> requestHajjPerson;
    private OnHajjPersonRequestListener listenerHajjPerson;

    public HajjTrackerRequest(OnHajjPersonRequestListener listenerHajjPerson) {
        apiClient = APIService.createService(ApiClient.class);
        this.listenerHajjPerson = listenerHajjPerson;
    }

    public void callApiHajjPerson(String app, String module, String action, String reqId){
        requestHajjPerson = apiClient.getHajjPerson(app, module, action, reqId);
        requestHajjPerson.enqueue(new Callback<HajjPerson>() {
            @Override
            public void onResponse(Call<HajjPerson> call, Response<HajjPerson> response) {
                System.out.println("onResponse " + response.raw().toString());
                if (response != null && response.isSuccessful()){
                    HajjPerson hajjPerson = response.body();
                    listenerHajjPerson.OnHajjTrackerRequestSuccess(hajjPerson);

                }else {
                    listenerHajjPerson.OnHajjTrackerRequestFailed("Response Invalid");
                }
            }

            @Override
            public void onFailure(Call<HajjPerson> call, Throwable t) {
                System.out.println("onFailure");
                String errorMessage = t.getMessage() != null?
                        t.getMessage() : "Unable to connect to server";
                listenerHajjPerson.OnHajjTrackerRequestFailed(errorMessage);
            }
        });
    }

    public interface OnHajjPersonRequestListener{
        void OnHajjTrackerRequestSuccess(HajjPerson hajjPersonResponse);
        void OnHajjTrackerRequestFailed(String message);
    }
}
