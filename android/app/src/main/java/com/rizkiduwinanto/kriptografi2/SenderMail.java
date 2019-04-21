package com.rizkiduwinanto.kriptografi2;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SenderMail extends AsyncTask<Void, Void, Void>{
    private Context context;
    private Session session;

    private String email;
    private String subject;
    private String message;
    private String emailFrom;
    private String password;

    private ProgressDialog progressDialog;

    public SenderMail(Context context, String emailFrom, String password, String email, String subject, String message){
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.emailFrom = emailFrom;
        this.password = password;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context,"Sending message","Please wait...",false,false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        Toast.makeText(context,"Message Sent",Toast.LENGTH_LONG).show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailFrom, password);
            }
        });

        try {
            MimeMessage mime = new MimeMessage(session);
            mime.setFrom(new InternetAddress(Config.EMAIL));
            mime.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mime.setSubject(subject);
            mime.setText(message);
            Transport.send(mime);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
