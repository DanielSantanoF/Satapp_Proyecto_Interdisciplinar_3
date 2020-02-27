package com.groupfive.satapp.ui.tickets;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.groupfive.satapp.R;
import com.groupfive.satapp.commons.Constants;
import com.groupfive.satapp.commons.MyApp;
import com.groupfive.satapp.models.tickets.TicketApiResponse;
import com.groupfive.satapp.retrofit.SatAppService;
import com.groupfive.satapp.retrofit.SatAppServiceGenerator;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewTicketDialogFragment extends DialogFragment {

    View v;
    Context ctx;
    Uri uri1Selected, uri2Selected;
    private static final int READ_REQUEST_CODE = Constants.IMAGE_READ_REQUEST_CODE;
    EditText edTitle, edDescription;
    Button btnUploadFoto1, btnUploadFoto2;
    ImageView ivFoto1Loaded, ivFoto2Loaded;
    Boolean isFoto1 = false;
    SatAppService service;


    public NewTicketDialogFragment(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getResources().getString(R.string.new_ticket_dialog_title));
        builder.setMessage(getResources().getString(R.string.new_ticket_dialog_message));

        builder.setCancelable(true);

        v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_new_ticket, null);
        builder.setView(v);

        edTitle = v.findViewById(R.id.editTextTitleNewTicket);
        edDescription = v.findViewById(R.id.editTextDescriptionNewTicket);
        btnUploadFoto1 = v.findViewById(R.id.buttonLoadFotoOneNewTicket);
        btnUploadFoto2 = v.findViewById(R.id.buttonLoadFotoTwoNewTicket);
        ivFoto1Loaded = v.findViewById(R.id.imageViewFotoOneNewTicket);
        ivFoto2Loaded = v.findViewById(R.id.imageViewFotoTwoNewTicket);

        uri1Selected = null;
        uri2Selected = null;

        btnUploadFoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFoto1 = true;
                performFileSearch();
            }
        });

        btnUploadFoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFoto1 = false;
                performFileSearch();
            }
        });

        builder.setPositiveButton(getResources().getString(R.string.create), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do sign up
                String title = edTitle.getText().toString();
                String description = edDescription.getText().toString();

                if(title.isEmpty() || description.isEmpty()) {
                    if(title.isEmpty()) {
                        edTitle.setError(getResources().getString(R.string.new_ticket_dialog_error_need_title));
                    }
                    if(description.isEmpty()) {
                        edDescription.setError(getResources().getString(R.string.new_ticket_dialog_error_need_description));
                    }
                } else {
                    if (uri1Selected != null || uri2Selected != null) {
                        try {
                            Activity act = (Activity) ctx;
                            //FOTOS 1
                            InputStream inputStream = act.getContentResolver().openInputStream(uri1Selected);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                            int cantBytes;
                            byte[] buffer = new byte[1024*4];

                            while ((cantBytes = bufferedInputStream.read(buffer,0,1024*4)) != -1) {
                                baos.write(buffer,0,cantBytes);
                            }

                            RequestBody requestFile =
                                    RequestBody.create(
                                            MediaType.parse(act.getContentResolver().getType(uri1Selected)), baos.toByteArray());

                            Cursor returnCursor = act.getContentResolver().query(uri1Selected, null, null, null, null);
                            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                            returnCursor.moveToFirst();
                            String fileName = returnCursor.getString(nameIndex);
                            MultipartBody.Part fotos1body =
                                    MultipartBody.Part.createFormData(Constants.FOTOS_NEW_TICKET_IMAGE_PART, fileName, requestFile);

                            //FOTOS2
                            InputStream inputStream2 = act.getContentResolver().openInputStream(uri2Selected);
                            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                            BufferedInputStream bufferedInputStream2 = new BufferedInputStream(inputStream2);
                            int cantBytes2;
                            byte[] buffer2 = new byte[1024*4];

                            while ((cantBytes2 = bufferedInputStream2.read(buffer2,0,1024*4)) != -1) {
                                baos2.write(buffer2,0,cantBytes2);
                            }

                            RequestBody requestFile2 =
                                    RequestBody.create(
                                            MediaType.parse(act.getContentResolver().getType(uri2Selected)), baos2.toByteArray());

                            Cursor returnCursor2 = act.getContentResolver().query(uri2Selected, null, null, null, null);
                            int nameIndex2 = returnCursor2.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                            returnCursor2.moveToFirst();
                            String fileName2 = returnCursor2.getString(nameIndex2);
                            MultipartBody.Part fotos2body =
                                    MultipartBody.Part.createFormData(Constants.FOTOS_NEW_TICKET_IMAGE_PART, fileName2, requestFile2);

                            //TEXT PARTS
                            RequestBody titleBody = RequestBody.create(MultipartBody.FORM, title);
                            RequestBody descriptionBody = RequestBody.create(MultipartBody.FORM, description);

                            //CALL TO API
                            service = SatAppServiceGenerator.createService(SatAppService.class);
                            Call<TicketApiResponse> callPostNewTicket = service.postNewTicket(fotos1body, fotos2body, titleBody, descriptionBody);

                            callPostNewTicket.enqueue(new Callback<TicketApiResponse>() {
                                @Override
                                public void onResponse(Call<TicketApiResponse> call, Response<TicketApiResponse> response) {
                                    if (response.isSuccessful()) {
                                        Log.d("Uploaded", "Éxito");
                                        Log.d("Uploaded", response.body().toString());
                                        Toast.makeText(MyApp.getContext(), getResources().getString(R.string.new_ticket_created), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.e("Upload error", response.errorBody().toString());
                                    }
                                }

                                @Override
                                public void onFailure(Call<TicketApiResponse> call, Throwable t) {
                                    Log.e("Upload error", t.getMessage());
                                }
                            });

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    dialog.dismiss();
                }
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }

    public void performFileSearch() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                if(isFoto1){
                    uri = resultData.getData();
                    Log.i("Filechooser URI", "Uri: " + uri.toString());
                    Glide
                            .with(this)
                            .load(uri)
                            .transform(new CircleCrop())
                            .into(ivFoto1Loaded);
                    uri1Selected = uri;
                    btnUploadFoto1.setEnabled(false);
                } else {
                    uri = resultData.getData();
                    Log.i("Filechooser URI", "Uri: " + uri.toString());
                    Glide
                            .with(this)
                            .load(uri)
                            .transform(new CircleCrop())
                            .into(ivFoto2Loaded);
                    uri2Selected = uri;
                    btnUploadFoto2.setEnabled(false);
                }
            }
        }
    }

}
