package cool.delia.router.submodule.java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import cool.delia.router.annotation.Router;

@Router(path = "/java/main", resultMsg = "hello java")
public class JavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java);
    }
}