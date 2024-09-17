package com.excal.simplerecyclerview

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.excal.simplerecyclerview.Data.AppDatabase
import com.excal.simplerecyclerview.Data.Transaction
import com.excal.simplerecyclerview.Data.TransactionViewModel
import com.excal.simplerecyclerview.Data.TransactionViewModelFactory
import com.excal.simplerecyclerview.databinding.ActivityMainBinding
import com.excal.simplerecyclerview.databinding.DialogWarningDeleteBinding
import com.excal.simplerecyclerview.databinding.EditDialogBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingDelete: DialogWarningDeleteBinding
    private lateinit var bindingEdit: EditDialogBinding

    private val transactionViewModel: TransactionViewModel by viewModels {
        TransactionViewModelFactory(AppDatabase.getInstance(applicationContext).transactionDao())
    }

    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Menampilkan daftar transaksi
        showTransaction()

        // Tombol untuk menambah perlombaan
        binding.btnAddCard.setOnClickListener {
            val intent = Intent(this, InputTransactionActivity::class.java)
            startActivity(intent)
        }
    }

    // Menampilkan daftar transaksi dalam RecyclerView
    private fun showTransaction() {
        binding.rvTransaction.layoutManager = LinearLayoutManager(this)
        transactionViewModel.transactionList.observe(this, Observer { transactions ->
            transactionAdapter = TransactionAdapter(transactions) { transaction, action ->
                when (action) {
                    "edit" -> {
                        showEditDialog(transaction)
                    }
                    "delete" -> {
                        showDeleteDialog(transaction)
                    }
                }
            }
            binding.rvTransaction.adapter = transactionAdapter
        })
        transactionViewModel.loadTransaction()
    }

    // Menampilkan dialog edit transaksi
    private fun showEditDialog(transaction: Transaction) {
        bindingEdit = EditDialogBinding.inflate(layoutInflater)
        val editTextTargetName = bindingEdit.editTextTargetName
        val editTextPerlombaan = bindingEdit.editTextPerlombaan
        val btnSave = bindingEdit.btnSave
        val btnCancel = bindingEdit.btnCancel

//        editTextAmount.setText(transaction.amount.toString())
        editTextTargetName.setText(transaction.targetName)
        editTextPerlombaan.setText(transaction.targetPerlombaan)

        val dialog = AlertDialog.Builder(this)
            .setView(bindingEdit.root)
            .setTitle("Edit Transaction")
            .create()
        dialog.show()

        btnSave.setOnClickListener {
            transactionViewModel.editTransactionById(
                transaction.targetId,
                editTextTargetName.text.toString(),
                editTextPerlombaan.text.toString()

            )
            dialog.dismiss()
            showTransaction()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    // Menampilkan dialog konfirmasi hapus transaksi
    private fun showDeleteDialog(transaction: Transaction) {
        bindingDelete = DialogWarningDeleteBinding.inflate(layoutInflater)
        val btnYes = bindingDelete.btnYes
        val btnNo = bindingDelete.btnNo

        val dialog = AlertDialog.Builder(this)
            .setView(bindingDelete.root)
            .setTitle("Delete Transaction")
            .create()
        dialog.show()

        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        btnYes.setOnClickListener {
            transactionViewModel.deleteTransactionById(transaction.targetId)
            dialog.dismiss()
            showTransaction()
        }
    }
}
