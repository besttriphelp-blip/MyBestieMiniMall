
package com.mybestieminimall

import android.app.*
import android.content.*
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendar = Calendar.getInstance()

        val titleInput = findViewById<EditText>(R.id.titleInput)
        val descInput = findViewById<EditText>(R.id.descInput)
        val dateButton = findViewById<Button>(R.id.dateButton)
        val timeButton = findViewById<Button>(R.id.timeButton)
        val saveButton = findViewById<Button>(R.id.saveButton)

        dateButton.setOnClickListener {
            DatePickerDialog(this,
                { _, y, m, d -> calendar.set(y, m, d) },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        timeButton.setOnClickListener {
            TimePickerDialog(this,
                { _, h, min ->
                    calendar.set(Calendar.HOUR_OF_DAY, h)
                    calendar.set(Calendar.MINUTE, min)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true).show()
        }

        saveButton.setOnClickListener {
            scheduleReminder(titleInput.text.toString(), descInput.text.toString())
            Toast.makeText(this, "Task Scheduled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun scheduleReminder(title: String, desc: String) {

        val intent = Intent(this, ReminderReceiver::class.java)
        intent.putExtra("title", title)
        intent.putExtra("desc", desc)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            calendar.timeInMillis.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}
