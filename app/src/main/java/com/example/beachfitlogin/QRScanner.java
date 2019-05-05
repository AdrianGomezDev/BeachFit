package com.example.beachfitlogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beachfitlogin.Interfaces.OnFragmentInteractionListener;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.app.Activity.RESULT_CANCELED;

public class QRScanner extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final int CAMERA_REQUEST = 2412;

    private OnFragmentInteractionListener mListener;

    private Button scanButton;
    private TextView barcodeResultText;
    private FirebaseFirestore mFirestore;

    public QRScanner() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QRScanner.
     */
    // TODO: Rename and change types and number of parameters
    public static QRScanner newInstance(String param1, String param2) {
        QRScanner fragment = new QRScanner();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("QR Scanner");
        View view = inflater.inflate(R.layout.fragment_qrscanner, container, false);

        barcodeResultText = view.findViewById(R.id.barcodeResultText);
        scanButton = view.findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Camera.class);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode != RESULT_CANCELED) {
            if (requestCode == CAMERA_REQUEST) {
                if (resultCode == CommonStatusCodes.SUCCESS) {
                    if (data != null) {
                        Barcode barcode = data.getParcelableExtra("barcode");
                        queryCollection(barcode);
                    } else {
                        barcodeResultText.setText("No barcode found");
                    }
                }

            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public void onScanResult(String exerciseName) {
        if (mListener != null) {
            mListener.onFragmentMessage("QR Scanner", exerciseName);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    // This function takes the displayValue of the barcode and check it against the Firestore db
    private void queryCollection(Barcode b)
    {
        DocumentReference exercise = mFirestore.collection("Exercises").document(b.displayValue);
        exercise.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        onScanResult(doc.get("Name").toString());
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"No matching QR code found", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(),"Error connecting to the database", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}