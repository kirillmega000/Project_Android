import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.company.maina.R
import com.example.company.maina.Recording
import com.example.company.maina.RecordingRepository
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_notifications.*

class NotificationsFragment : Fragment() {
    private lateinit var groupAdapter: GroupAdapter<ViewHolder>
    private var data: ArrayList<String>? = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)



    }

    companion object {

        fun newInstance(): NotificationsFragment {
            return NotificationsFragment()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        groupAdapter = GroupAdapter<ViewHolder>()
        updateAdapter()
        val apply = recordings_recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = groupAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        updateAdapter()
        val apply = recordings_recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = groupAdapter
        }
    }
    private fun updateAdapter() {
        data = RecordingRepository.getInstance().getRecordings()
        println("Updating Adapter")
        groupAdapter.clear()

        if(data != null) {
            data!!.forEach {
                println("Data: $it")
                groupAdapter.add(Recording(it,context?:return))
            }
        }
    }

    override fun onPause() {
        try {
            RecordingRepository.mediaPlayer?.stop()
        }catch (e:Exception){
            e.printStackTrace()
        }
        super.onPause()
    }
    }


