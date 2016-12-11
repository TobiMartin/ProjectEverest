package com.linxu.mounteverest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by lin xu on 02.12.2016.
 */
public class AddProject extends AppCompatActivity {
    private Button save;
    private EditText learningStepPercent1;
    private EditText learningStepPercent2;
    private EditText learningStepPercent3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_project);
        learningStepPercent1 = (EditText)findViewById(R.id.learningStepPercent1);
        learningStepPercent2 = (EditText)findViewById(R.id.learningStepPercent2);
        learningStepPercent3 = (EditText)findViewById(R.id.learningStepPercent3);

        save = (Button)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProject();
            }
        });
    }

    private void saveProject() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("learningStepPercent1", learningStepPercent1.getText().toString());
        intent.putExtra("learningStepPercent2", learningStepPercent2.getText().toString());
        intent.putExtra("learningStepPercent3", learningStepPercent3.getText().toString());
        startActivity(intent);
    }
}
