#### Setting Up KYC Module in Kotlin:
- Open the existing project you wish to use KYC Module on in Android Studio
- Copy the app folder of the module to the project root (Same level with the original  app
- On the Android Studio pop up windows, rename the module to a different name  (module_name) other than the app directory
- Include the module into your existing project. Open settings.gradle.kts      and add  `include(":module_name")` at the end of the file
- Import the module into the gradle configurations. Open app/build.gradle    and add the module implementation  implementation(project(":module_name"))   under dependecies
- Build the project to test if there is any errors in dependencies

#### Usage:
- Create an intent that registers for result on the Activity you wish to use the results from the KYC Module. The module returns a data class KYCResult ``
`KYCResult`
```
data class KYCResult(var type: String) : Parcelable {
var text: String? = null;
var barcodes: List<String>? = null;
var faces: List<String?>? = null;
}
```
Launcher Intent:

```
    val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                var data: KYCResult? = result.data?.getParcelableExtra<KYCResult>("data")
                if(data != null){
                // Results from the detection will be  available in data class based on the selected processor
                    Log.d("RESULT","Returned data is of type: ${data.type} with data ${data.text?: data.barcodes?: data.faces}")

                }
            }
        }
 ```

Launch the Activity:
`intentLauncher.launch(Intent(this, StillImageActivity::class.java))`
