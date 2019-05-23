package com.example.laloinsane.austral_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_campus_menu;
    private AlertDialog exit_dialog;
    private Button btn_acerca;
    private Dialog acerca;
    private Button btn_acerca_aceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        setupView();
        createDialog();
    }

    public void init() {
        btn_campus_menu = (Button) findViewById(R.id.btn_campus_menu);
        btn_acerca = (Button) findViewById(R.id.btn_acerca);
    }

    public void setupView(){
        btn_campus_menu.setOnClickListener(this);
        btn_acerca.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        exit_dialog = new AlertDialog.Builder(this).create();
        exit_dialog.setMessage("¿Estas seguro de querer salir?");
        exit_dialog.setButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                exit();
            }
        });
        exit_dialog.setButton2("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                exit_dialog.dismiss();
            }
        });
        exit_dialog.show();
    }

    public void exit(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btn_campus_menu: {
                Intent intent=new Intent(HomeActivity.this, CampusMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
            }
            case R.id.btn_acerca: {
                acerca.show();
                break;
            }
        }
    }

    protected void createDialog(){
        acerca = new Dialog(this);
        acerca.requestWindowFeature(Window.FEATURE_NO_TITLE);
        acerca.setContentView(R.layout.layout_acerca);
        acerca.setCanceledOnTouchOutside(true);
        acerca.setCancelable(true);
        btn_acerca_aceptar = (Button) acerca.findViewById(R.id.btn_acerca_aceptar);
        btn_acerca_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acerca.dismiss();
            }
        });
    }

}
