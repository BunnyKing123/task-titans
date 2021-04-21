package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import xyz.dolphcode.tasktitans.fragments.DatePickerFragment;
import xyz.dolphcode.tasktitans.resources.FrequencyType;
import xyz.dolphcode.tasktitans.util.DateTimeActivity;
import xyz.dolphcode.tasktitans.util.Util;

public class FrequencyPickerActivity extends DateTimeActivity {

    String sentDate = "";
    int[] currentDate;

    Intent prev;

    int monthFlags = 0b000000000000;
    int dayFlags = 0b0000000;

    EditText weeksTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequency_picker);

        Util.setupConnectionChangedHandler(FrequencyPickerActivity.this);

        prev = getIntent();

        date = findViewById(R.id.frequencyDateTxt);
        datePicker = new DatePickerFragment();
        currentDate = Util.currentDate();
        sentDate = currentDate[0] + "|" + currentDate[1] + "|" + currentDate[2];
        date.setText(Util.formatDate(currentDate[0], currentDate[1], currentDate[2]));

        weeksTxt = findViewById(R.id.freqWeeksInputTxt);

        addMonthListeners();
        addDateListeners();

        Button month = findViewById(R.id.monthsPick);
        Button date = findViewById(R.id.datePick);
        Button day = findViewById(R.id.daysPick);
        Button week = findViewById(R.id.weeksPick);

        week.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FrequencyPickerActivity.this, RepeatTaskActivity.class);
                int weeks;
                try {
                    weeks = Integer.parseInt(weeksTxt.getText().toString());
                } catch (NumberFormatException e) {
                    weeks = 1;
                }
                sendData(intent, FrequencyType.WEEKS, "" + weeks);
                FrequencyPickerActivity.this.startActivity(intent);
            }
        });

        day.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FrequencyPickerActivity.this, RepeatTaskActivity.class);
                if (dayFlags == 0) {
                    sendData(intent, FrequencyType.DAYS, "0000000");
                } else {
                    String monthData = String.format("%7s", Integer.toBinaryString(monthFlags)).replace(' ', '0');
                    sendData(intent, FrequencyType.DAYS, monthData);
                }
                FrequencyPickerActivity.this.startActivity(intent);
            }
        });

        month.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FrequencyPickerActivity.this, RepeatTaskActivity.class);
                if (monthFlags == 0) {
                    sendData(intent, FrequencyType.MONTHS, "000000000000");
                } else {
                    String monthData = String.format("%12s", Integer.toBinaryString(monthFlags)).replace(' ', '0');
                    sendData(intent, FrequencyType.MONTHS, monthData);
                }
                FrequencyPickerActivity.this.startActivity(intent);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FrequencyPickerActivity.this, RepeatTaskActivity.class);
                sendData(intent, FrequencyType.DATE, sentDate);
                FrequencyPickerActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    public void sendDate(int day, int month, int year) {
        date.setText(Util.formatDate(day, month, year));
        sentDate = day + "-" + month + "-" + year;
    }

    @Override
    public void sendTime(int hour, int min) {} // Not needed so left blank

    // Sends data to the next screen
    public void sendData(Intent intent, int freqType, String freqData) {
        Util.transferData(prev, intent);
        intent.putExtra("FREQTYPE", freqType);
        intent.putExtra("FREQDATA", freqData);
    }

    // Adds listeners to each of the month checkboxes to set the flags
    public void addMonthListeners() {
        CheckBox jan = findViewById(R.id.janBox);
        CheckBox feb = findViewById(R.id.febBox);
        CheckBox mar = findViewById(R.id.marBox);
        CheckBox apr = findViewById(R.id.aprBox);
        CheckBox may = findViewById(R.id.mayBox);
        CheckBox jun = findViewById(R.id.junBox);
        CheckBox jul = findViewById(R.id.julBox);
        CheckBox aug = findViewById(R.id.augBox);
        CheckBox sep = findViewById(R.id.septBox);
        CheckBox oct = findViewById(R.id.octBox);
        CheckBox nov = findViewById(R.id.novBox);
        CheckBox dec = findViewById(R.id.decBox);

        jan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) monthFlags = monthFlags | 0b100000000000; else monthFlags = monthFlags & 0b011111111111;
            }});

        feb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) monthFlags = monthFlags | 0b010000000000; else monthFlags = monthFlags & 0b101111111111;
            }});

        mar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) monthFlags = monthFlags | 0b001000000000; else monthFlags = monthFlags & 0b110111111111;
            }});

        apr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) monthFlags = monthFlags | 0b000100000000; else monthFlags = monthFlags & 0b111011111111;
            }});

        may.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) monthFlags = monthFlags | 0b000010000000; else monthFlags = monthFlags & 0b111101111111;
            }});

        jun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) monthFlags = monthFlags | 0b000001000000; else monthFlags = monthFlags & 0b111110111111;
            }});

        jul.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) monthFlags = monthFlags | 0b000000100000; else monthFlags = monthFlags & 0b111111011111;
            }});

        aug.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) monthFlags = monthFlags | 0b000000010000; else monthFlags = monthFlags & 0b111111101111;
            }});

        sep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) monthFlags = monthFlags | 0b000000001000; else monthFlags = monthFlags & 0b111111110111;
            }});

        oct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) monthFlags = monthFlags | 0b000000000100; else monthFlags = monthFlags & 0b111111111011;
            }});

        nov.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) monthFlags = monthFlags | 0b000000000010; else monthFlags = monthFlags & 0b111111111101;
            }});

        dec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) monthFlags = monthFlags | 0b000000000001; else monthFlags = monthFlags & 0b111111111110;
            }});
    }

    public void addDateListeners() {
        CheckBox sunday = findViewById(R.id.sundayBox);
        CheckBox monday = findViewById(R.id.mondayBox);
        CheckBox tuesday = findViewById(R.id.tuesdayBox);
        CheckBox wednesday = findViewById(R.id.wednesdayBox);
        CheckBox thursday = findViewById(R.id.thursdayBox);
        CheckBox friday = findViewById(R.id.fridayBox);
        CheckBox saturday = findViewById(R.id.saturdayBox);

        sunday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) dayFlags = dayFlags | 0b1000000; else dayFlags = dayFlags & 0b0111111;
            }});

        monday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) dayFlags = dayFlags | 0b0100000; else dayFlags = dayFlags & 0b1011111;
            }});

        tuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) dayFlags = dayFlags | 0b0010000; else dayFlags = dayFlags & 0b1101111;
            }});

        wednesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) dayFlags = dayFlags | 0b0001000; else dayFlags = dayFlags & 0b1110111;
            }});

        thursday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) dayFlags = dayFlags | 0b0000100; else dayFlags = dayFlags & 0b1111011;
            }});

        friday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) dayFlags = dayFlags | 0b0000010; else dayFlags = dayFlags & 0b1111101;
            }});

        saturday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) dayFlags = dayFlags | 0b0000001; else dayFlags = dayFlags & 0b1111110;
            }});
    }

}
