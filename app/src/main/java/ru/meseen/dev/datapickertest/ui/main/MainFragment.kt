package ru.meseen.dev.datapickertest.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import ru.meseen.dev.datapickertest.R

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private lateinit var message: TextView
    private lateinit var imageView: ImageView
    private lateinit var storageRead: StorageRead

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        storageRead = StorageRead(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        message = view.findViewById<TextView>(R.id.message)?.apply {
            setOnClickListener {
                storageRead.requestLauncher.launch(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "*/*"
                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                })
            }

        }!!
        imageView = view.findViewById(R.id.imageView)
        storageRead.listPickedFiles.observe(viewLifecycleOwner) {
            val text  = it.toString() + "\n всего ${it.size} \n вес ${it.fold(0L){oz,os -> os.nameAndSize.second+ oz} } byte"
            message.text = text
            val uri = it.random().uri
            Glide.with(imageView).load(uri).into(imageView) // просто так хз что буде с не изображением

        }

    }
}


