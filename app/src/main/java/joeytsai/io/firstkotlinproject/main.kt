package joeytsai.io.firstkotlinproject

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class main : Activity() {

    lateinit var repoTable: TableLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repoTable = findViewById(R.id.repoTable)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
        val service = retrofit.create(GitHubService::class.java)
        val request = service.listRepos("joeytsai301")
        request.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                        repos : List<Repo> -> repos.forEach{
                            repo -> addRow(repo.name)
                }
                },
                {
                    Log.d("dev","Error")
                },
            )
//        val call = service.listRepos("joeytsai301")
//        call.enqueue( object : Callback<List<Repo>> {
//
//            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
//                //API回傳結果
//                response.body()?.forEach { repo ->
//                    addRow(repo.name)
//                }
//            }
//            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
//                Log.d("DEV", "error")
//            }
//        })
    }

    fun addRow(name: String){

        val row = LayoutInflater.from(this).inflate(R.layout.row, null) as TableRow
        val title = row.findViewById(R.id.row_title) as TextView
        title.setText(name)
        repoTable.addView(row)
    }

}