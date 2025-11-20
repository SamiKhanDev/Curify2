import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.saim.domain.entities.Doctor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DoctorsViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _data = MutableStateFlow<List<Doctor>>(emptyList())
    val data = _data.asStateFlow()

    init {
        loadDoctors()
    }

    private fun loadDoctors() {
        db.collection("Doctors")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { it.toObject(Doctor::class.java) }
                    _data.value = list
                }
            }
    }
}
