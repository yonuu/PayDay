package com.example.lg.payday;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.Manifest;
import android.nfc.*;
import android.nfc.tech.*;
import android.os.*;
import android.provider.*;
import android.app.*;
import android.content.*;
import android.support.v7.app.AppCompatActivity;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.lg.payday.R.id.item_textview;


public class NfcActivity extends AppCompatActivity {

    TextView nresult;
    NfcAdapter nfcAdapter;
    private FirebaseDatabase database;
    DataSnapshot dataSnapshot;
    public int res;
    public String ago;
    public String now;
    public Date trans_ago;
    public Date trans_now;
    String workspace1;
    long total;
    int wage;
    long result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_nfc);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        String workspace = ListActivity.dtos.get(ListActivity.lstNum).workspace;
        if(nfcAdapter!=null && nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC available!", Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(this, "NFC not availble:(", Toast.LENGTH_LONG).show();
        }
        total = ListActivity.dtos.get(ListActivity.lstNum).total;
        Log.w("test",String.valueOf(total));
        wage = Integer.parseInt(ListActivity.dtos.get(ListActivity.lstNum).wage);
        result = wage*((total/3600000)%24);
        String resultn = String.valueOf(result);
        nresult = (TextView)findViewById(R.id.result);
        nresult.setText(resultn);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }


    }

    protected void noNewIntent(Intent intent){
        super.onNewIntent(intent);
        if(intent.hasExtra(NfcAdapter.EXTRA_TAG)){
            Toast.makeText(this, "NfcIntent!", Toast.LENGTH_LONG).show();
        }

    }

    protected void onResume() { // nfc 태그시

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String time = sdf.format(new Date(System.currentTimeMillis()));

        int state = ListActivity.dtos.get(ListActivity.lstNum).state;
        workspace1 = ListActivity.dtos.get(ListActivity.lstNum).workspace;

        if(state == 0) {
            Log.w("test","nig");
            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                if(snapshot.getValue(DTO.class).workspace.equals(workspace1)){
                                    int state = snapshot.getValue(DTO.class).state;
                                    String ago = snapshot.getValue(DTO.class).ago=sdf.format(new Date(System.currentTimeMillis()));
                                    long start = System.currentTimeMillis();

                                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(ListActivity.dtos.get(ListActivity.lstNum).workspace).child("state").setValue((state+1)%2);
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(ListActivity.dtos.get(ListActivity.lstNum).workspace).child("ago").setValue(ago);
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(ListActivity.dtos.get(ListActivity.lstNum).workspace).child("start").setValue(start);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
        else if(state == 1) {
            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (snapshot.getValue(DTO.class).workspace.equals(workspace1)) ;
                                {
                                    int state = snapshot.getValue(DTO.class).state = 0;


                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String time = sdf.format(new Date(System.currentTimeMillis()));
                                    String now = time;
                                    long end = System.currentTimeMillis();
                                    long agototal = snapshot.getValue(DTO.class).total;
                                    long total = agototal + (end - snapshot.getValue(DTO.class).start);

                                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(ListActivity.dtos.get(ListActivity.lstNum).workspace).child("state").setValue(state);
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(ListActivity.dtos.get(ListActivity.lstNum).workspace).child("now").setValue(now);
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(ListActivity.dtos.get(ListActivity.lstNum).workspace).child("end").setValue(end);
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(ListActivity.dtos.get(ListActivity.lstNum).workspace).child("total").setValue(total);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

        Intent intent = new Intent(this, StartActivity.class);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,0);
        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
        super.onResume();
        enableForegroundDispatchSystem();
    }

    protected void onPause() {

        nfcAdapter.disableForegroundNdefPush(this);
        disableForegroundDispatchSystem();
        super.onPause();
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }


    private String getTextFromNdefRecord(NdefRecord ndefRecord) {
        String tagContent = null;
        try{
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1,
                    payload.length - languageSize -1, textEncoding);

        }catch(UnsupportedEncodingException e){
            Log.e("getTextFromNdefRecord", e.getMessage(), e);
        }
        return tagContent;
    }

    private void enableForegroundDispatchSystem () {
        Log.w("tessss","ww");
        Intent intent = new Intent(this, StartActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);}


    private void disableForegroundDispatchSystem(){
        nfcAdapter.disableForegroundDispatch(this);
    }

    private void formatTag(Tag tag, NdefMessage ndefMessage){
        try{
            NdefFormatable ndefFormatable = NdefFormatable.get(tag);
            if(ndefFormatable == null){
                Toast.makeText(this, "Tag is not ndef formatable!", Toast.LENGTH_SHORT).show();

                ndefFormatable.connect();
                ndefFormatable.format(ndefMessage);
                ndefFormatable.close();
                Toast.makeText(this, "Tag is writen!", Toast.LENGTH_SHORT).show();
                return;
            }
        }catch (Exception e){
            Log.e("formatTag", e.getMessage());
        }

    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage){

        try{
            if(tag == null)
            {
                Toast.makeText(this, "Tag object cannot be null", Toast.LENGTH_SHORT).show();
                return;
            }


            Ndef ndef = Ndef.get(tag);
            if(ndef == null)
            {
                // format tag with the ndef format and writes the message
                formatTag(tag, ndefMessage);
            }else
            {
                ndef.connect();
                if(!ndef.isWritable()){
                    Toast.makeText(this, "tag is not writable!", Toast.LENGTH_SHORT).show();
                    ndef.close();
                    return;
                }

                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
                Toast.makeText(this, "Tag is writen!", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Log.e("Write tag!", e.getMessage());
        }

    }

    private NdefRecord createTextRecord(String content){
        try {
            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes("UTF-8");
            final byte[] text = content.getBytes("UTF-8");
            final int languageSize = language.length;
            final int textLength = text.length;
            final ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + languageSize + textLength);

            payload.write((byte) (languageSize & 0x1F));
            payload.write(language, 0, languageSize);
            payload.write(text, 0, textLength);

            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());

        } catch(UnsupportedEncodingException e){
            Log.e("createTextRecord", e.getMessage());
        }
        return null;
    }

    private NdefMessage createNdefMessage(String content){

        NdefRecord ndefRecord = createTextRecord(content);

        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord });

        return ndefMessage;

    }
}
