package sa.med.imc.myimc.SYSQUO.Chat.chat;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import sa.med.imc.myimc.R;

public class MediaSelectionFragment  extends BottomSheetDialogFragment //implements View.OnClickListener
{
    
    MainChatActivity_New mainChatActivity_new;
    private View view;
    LinearLayout linearCameraAttach, linearTextAttach, linearPdfAttach;
    ImageView imageCameraAttach, imageTextAttach, imagePdfAttach;
    TextView textCameraAttach, textTextAttach, textPdfAttach;
    Context mContext;
    AttachmentSelectionInterface attachmentSelectionInterface;
    public MediaSelectionFragment(MainChatActivity_New mainChatActivity_new, AttachmentSelectionInterface attachmentSelectionInterface) {
        this.mainChatActivity_new = mainChatActivity_new;
        this.mContext   =   mainChatActivity_new;
        this.attachmentSelectionInterface = attachmentSelectionInterface;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.sysquo_attach_file, null);
        dialog.setContentView(contentView);
        view = (View) contentView.getParent();


        linearCameraAttach = view.findViewById(R.id.linearCameraAttach);
        linearTextAttach      = view.findViewById(R.id.linearTextAttach);
        linearPdfAttach      = view.findViewById(R.id.linearPdfAttach);

        imageCameraAttach = view.findViewById(R.id.imageCameraAttach);
        imageTextAttach      = view.findViewById(R.id.imageTextAttach);
        imagePdfAttach      = view.findViewById(R.id.imagePdfAttach);

        textCameraAttach = view.findViewById(R.id.textCameraAttach);
        textTextAttach      = view.findViewById(R.id.textTextAttach);
        textPdfAttach      = view.findViewById(R.id.textPdfAttach);

        linearCameraAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachmentSelectionInterface.getAttachMediaType(getResources().getString(R.string.image_text));
                dialog.dismiss();
            }
        });
        linearPdfAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachmentSelectionInterface.getAttachMediaType(getResources().getString(R.string.pdf));
                dialog.dismiss();
            }
        });
        linearTextAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachmentSelectionInterface.getAttachMediaType(getResources().getString(R.string.text));
                dialog.dismiss();
            }
        });

        imageCameraAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachmentSelectionInterface.getAttachMediaType(getResources().getString(R.string.image_text));
                dialog.dismiss();
            }
        });
        imagePdfAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachmentSelectionInterface.getAttachMediaType(getResources().getString(R.string.pdf));
                dialog.dismiss();
            }
        });
        imageTextAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachmentSelectionInterface.getAttachMediaType(getResources().getString(R.string.text));
                dialog.dismiss();
            }
        });

        textCameraAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachmentSelectionInterface.getAttachMediaType(getResources().getString(R.string.image_text));
                dialog.dismiss();
            }
        });
        textPdfAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachmentSelectionInterface.getAttachMediaType(getResources().getString(R.string.pdf));
                dialog.dismiss();
            }
        });
        textTextAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachmentSelectionInterface.getAttachMediaType(getResources().getString(R.string.text));
                dialog.dismiss();
            }
        });

    }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
   /* @Override
    public void onClick(View v) {
        if(v.getId() == R.id.linearCameraAttach)
        {
            attachmentSelectionInterface.getAttachMediaType(getResources().getString(R.string.image_text));
        }
        if(v.getId() == R.id.linearPdfAttach)
        {
            attachmentSelectionInterface.getAttachMediaType(getResources().getString(R.string.pdf));
        }
        if(v.getId() == R.id.linearTextAttach)
        {
            attachmentSelectionInterface.getAttachMediaType(getResources().getString(R.string.text));
        }
    }*/
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
}

