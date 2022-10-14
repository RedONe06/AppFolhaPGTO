package br.ulbra.appfolhapgto;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText edtNome, edtSalarioBruto, edtNumeroFilhos;
    RadioGroup radioGroup;
    RadioButton radioFeminino, radioMasculino;
    TextView txtResultado;
    Button btnCalcular;


    List dadosDaPessoa = new ArrayList();
    // {nome, salario, sexo, nDeFilhos}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtNome = findViewById(R.id.edtNome);
        edtSalarioBruto = findViewById(R.id.edtSalarioBruto);
        edtNumeroFilhos = findViewById(R.id.edtNumeroFilhos);
        btnCalcular = findViewById(R.id.btnCalcular);
        radioGroup = findViewById(R.id.radioGroupSexo);
        radioFeminino = findViewById(R.id.radioFeminino);
        radioMasculino = findViewById(R.id.radioMasculino);
        txtResultado = findViewById(R.id.txtResultado);

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean camposEstaoCompletos =
                        !edtNumeroFilhos.getText().toString().equals("") && !edtSalarioBruto.getText().toString().equals("");

                if(camposEstaoCompletos){

                    adicionarDados();

                    boolean saoDadosValidos = (Double) dadosDaPessoa.get(1) >= 0 && (Double) dadosDaPessoa.get(3) >= 0;

                    if(saoDadosValidos){
                        double INSS = calcularINSS((Double) dadosDaPessoa.get(1));
                        double IR = calcularIR((Double) dadosDaPessoa.get(1));
                        double salarioFamilia = calcularSalarioFamilia((Double) dadosDaPessoa.get(1), (Double) dadosDaPessoa.get(3));
                        double salarioLiquido = calcularSalarioLiquido((Double) dadosDaPessoa.get(1), INSS, IR, salarioFamilia);
                        String resultado = gerarResultado((String)dadosDaPessoa.get(0),
                                (Integer) dadosDaPessoa.get(2),
                                INSS,
                                IR,
                                salarioLiquido);
                        txtResultado.setText(resultado);
                        dadosDaPessoa.clear();
                    } else {
                        Toast.makeText(MainActivity.this, "Dados inválidos, tente novamente.",
                                Toast.LENGTH_LONG).show();
                        dadosDaPessoa.clear();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Dados inválidos, tente novamente.",
                            Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    public void adicionarDados() {
        dadosDaPessoa.add(edtNome.getText().toString());
        dadosDaPessoa.add(Double.parseDouble(edtSalarioBruto.getText().toString()));
        if (radioMasculino.isChecked()) {
            dadosDaPessoa.add(1);
        } else if (radioFeminino.isChecked())
            dadosDaPessoa.add(2);
        else {
            dadosDaPessoa.add(3);
        }
        dadosDaPessoa.add(Double.parseDouble(edtNumeroFilhos.getText().toString()));
    }

    public double calcularINSS(double salarioBruto) {
        double INSS;
        if (salarioBruto <= 1212.00) {
            INSS = salarioBruto * 0.075;
        } else if (salarioBruto < 2427.35) {
            INSS = salarioBruto * 0.09;
        } else if (salarioBruto < 3641.03) {
            INSS = salarioBruto * 0.12;
        } else {
            INSS = salarioBruto * 0.14;
        }
        return INSS;
    }

    public double calcularSalarioFamilia(double salarioBruto, double quantFilhos){
        double salarioFamilia;
        if(salarioBruto <= 1212){
            salarioFamilia = quantFilhos * 56.47;
        } else {
            salarioFamilia = 0;
        }
        return salarioFamilia;
    }

    public double calcularIR(double salarioBruto) {
        double IR;
        if (salarioBruto <= 1903.98) {
            IR = 0;
        } else if (salarioBruto < 2826.65) {
            IR = salarioBruto * 0.075;
        } else if (salarioBruto < 3751.05) {
            IR = salarioBruto * 0.15;
        } else {
            IR = salarioBruto * 0.225;
        }
        return IR;
    }

    public double calcularSalarioLiquido(double salarioBruto, double INSS, double IR, double salarioFamilia) {
        double salarioLiquido = salarioBruto - INSS - IR + salarioFamilia;
        return salarioLiquido;
    }

    public String gerarResultado(String nome, int sexo, double INSS, double IR, double salarioLiquido) {
        String formalidade;
        switch (sexo) {
            case 1:
                formalidade = "Sr.";
                break;
            case 2:
                formalidade = "Sra.";
                break;
            default:
                formalidade = "Prezado(a)";
                break;
        }
        String resultado = formalidade + " " + nome +
                "\nINSS: R$ " + INSS +
                "\nIR: R$ " + IR +
                "\nSalario líquido: R$ " + salarioLiquido +
                "\n{get(0): " + dadosDaPessoa.get(0) +
                ", get(1): " + dadosDaPessoa.get(1) +
                ", get(2): " + dadosDaPessoa.get(2) +
                ", get(3): " + dadosDaPessoa.get(3);
        return resultado;
    }
}