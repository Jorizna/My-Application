package com.jorge.colorapplication

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jorge.colorapplication.R

class ColorsActivity : AppCompatActivity() {

    private val h1 = ColorCard("H1", 20, Color.Orange)
    private val h2 = ColorCard("H2", 50, Color.Violet)
    private val h3 = ColorCard("H3", 80, Color.Blue)

    private val v1 = ColorCard("V1", 20, Color.Yellow)
    private val v2 = ColorCard("V2", 35, Color.Orange)
    private val v3 = ColorCard("V3", 50, Color.Yellow)
    private val v4 = ColorCard("V4", 65, Color.Green)
    private val v5 = ColorCard("V5", 80, Color.Cyan)

    private val verticalCards = listOf(v1, v2, v3, v4, v5)

    // Views
    private lateinit var cardH1: CardView
    private lateinit var cardH2: CardView
    private lateinit var cardH3: CardView
    private lateinit var btnChangeColor: CardView
    private lateinit var recyclerView: RecyclerView
    private lateinit var barAdapter: BarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_colors)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initComponents()
        initListeners()
        initUI()
    }

    private fun initComponents() {
        cardH1 = findViewById(R.id.cardH1)
        cardH2 = findViewById(R.id.cardH2)
        cardH3 = findViewById(R.id.cardH3)
        btnChangeColor = findViewById(R.id.btnChangeColor)
        recyclerView = findViewById(R.id.recyclerView)
    }

    private fun initListeners() {
        btnChangeColor.setOnClickListener { showDialog() }
    }

    private fun initUI() {
        barAdapter = BarAdapter(verticalCards)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = barAdapter

        updateColors()
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_color)

        val rgRows: RadioGroup = dialog.findViewById(R.id.rgRows)
        val rgColors: RadioGroup = dialog.findViewById(R.id.rgColors)
        val btnApply: Button = dialog.findViewById(R.id.btnAddGame)

        btnApply.setOnClickListener {

            val selectedCardId = rgRows.checkedRadioButtonId
            val selectedColorId = rgColors.checkedRadioButtonId

            val selectedCard: ColorCard = when (selectedCardId) {
                R.id.rbH1 -> h1
                R.id.rbH2 -> h2
                R.id.rbH3 -> h3
                R.id.rbV1 -> v1
                R.id.rbV2 -> v2
                R.id.rbV3 -> v3
                R.id.rbV4 -> v4
                else -> v5
            }

            val selectedColor: Color = when (selectedColorId) {
                R.id.rgWhite -> Color.White
                R.id.rgRed -> Color.Red
                R.id.rbOrange -> Color.Orange
                R.id.rbYellow -> Color.Yellow
                R.id.rbGreen -> Color.Green
                R.id.rbCyan -> Color.Cyan
                R.id.rbBlue -> Color.Blue
                R.id.rbViolet -> Color.Violet
                else -> Color.Black
            }

            selectedCard.color = selectedColor
            updateColors()
            dialog.hide()
        }
        dialog.show()
    }

    private fun updateColors() {
        paintCard(cardH1, h1)
        paintCard(cardH2, h2)
        paintCard(cardH3, h3)
        barAdapter.notifyDataSetChanged()
    }

    private fun paintCard(cardView: CardView, colorCard: ColorCard) {
        val colorRes = colorCard.color.getRes(colorCard.opacity)
        cardView.setCardBackgroundColor(ContextCompat.getColor(this, colorRes))
    }
}