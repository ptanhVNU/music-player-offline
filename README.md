# Ứng Dụng Nghe Nhạc Offline

## Bối Cảnh Phát Triển Ứng Dụng

Ứng dụng âm nhạc offline đã trở thành một phần không thể thiếu của cuộc sống hiện đại, mang lại tầm quan trọng không chỉ trong việc tiêu thụ âm nhạc mà còn trong việc tạo ra trải nghiệm âm nhạc cá nhân và mở rộng phạm vi tiếp cận với nền âm nhạc đa dạng của thế giới. Đây không chỉ là một cách tiện lợi để nghe nhạc mà còn mang lại nhiều giá trị tinh thần và xã hội đáng kể.

Việc sử dụng ứng dụng âm nhạc offline giúp tiết kiệm dung lượng dữ liệu di động. Người dùng có thể tải xuống âm nhạc từ các dịch vụ streaming để nghe khi không có kết nối mạng, tránh việc tiêu tốn dung lượng dữ liệu khi phát trực tuyến. Có thể truy cập danh sách phát yêu thích và nghe nhạc ngay cả khi không có kết nối internet. Điều này rất hữu ích trong các hoàn cảnh như khi đi du lịch, đi trên các phương tiện giao thông công cộng hoặc ở những khu vực có tín hiệu kém.

Nghe nhạc offline có thể giúp tiết kiệm pin và tối ưu hóa hiệu suất của thiết bị di động. Không phải liên tục kết nối internet cũng giúp trải nghiệm nghe nhạc được mượt mà hơn. Sử dụng âm nhạc offline giúp bảo vệ quyền riêng tư của người dùng, vì thông tin về việc nghe nhạc không cần phải được truyền đi hoặc lưu trữ trên các máy chủ từ xa. Người dùng sẽ không lo lắng bị tiết lộ thông tin cá nhân.

Chức năng lưu trữ âm nhạc offline thường có chất lượng tốt hơn so với việc phát trực tuyến, vì không gặp vấn đề về kết nối mạng hoặc việc nén dữ liệu như khi phát trực tuyến. Tóm lại, việc sử dụng ứng dụng âm nhạc offline mang lại sự tiện ích, tiết kiệm dữ liệu, tăng tính linh hoạt và bảo mật cho người dùng. Nó cũng cung cấp trải nghiệm nghe nhạc mượt mà và chất lượng tốt hơn trong nhiều tình huống khác nhau.

## 💻 Tech Stacks

Dự án này sử dụng nhiều thư viện, plugin và công cụ phổ biến của hệ sinh thái Android.

- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Jetpack Compose là bộ công cụ hiện đại được Android khuyến nghị để xây dựng giao diện người dùng native. Nó giúp đơn giản hóa và tăng tốc quá trình phát triển UI trên Android.

- [Material3](https://developer.android.com/jetpack/androidx/releases/compose-material3) - Xây dựng UI Jetpack Compose với các thành phần thiết kế sẵn sàng sử dụng của Material Design.

- [Foundation](https://developer.android.com/jetpack/androidx/releases/compose-foundation) - Viết ứng dụng Jetpack Compose với các khối xây dựng sẵn sàng sử dụng và mở rộng foundation để xây dựng các phần hệ thống thiết kế riêng.

- [UI](https://developer.android.com/jetpack/androidx/releases/compose-ui) - Các thành phần cơ bản của compose UI cần thiết để tương tác với thiết bị, bao gồm bố cục, vẽ và đầu vào.

- [Android KTX](https://developer.android.com/kotlin/ktx.html) - Cung cấp Kotlin súc tích và ngôn ngữ tự nhiên cho Jetpack và các API nền tảng Android.

- [AndroidX](https://developer.android.com/jetpack/androidx) - Cải thiện lớn so với thư viện [Support Library](https://developer.android.com/topic/libraries/support-library/index) gốc, không còn được duy trì.

- [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) - Thực hiện các hành động phản ứng với sự thay đổi trong trạng thái lifecycle của các thành phần khác nhau, như activities và fragments.

- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Thiết kế để lưu trữ và quản lý dữ liệu liên quan đến UI một cách tự ý thức với lifecycle. Lớp ViewModel cho phép dữ liệu tồn tại qua các thay đổi cấu hình như xoay màn hình.

- [Media3 ExoPlayer](https://developer.android.com/guide/topics/media/exoplayer) - Một trình phát phương tiện là một thành phần ứng dụng cấp cao cho phép phát lại video và âm thanh. Các tệp như vậy có thể được lưu trữ cục bộ hoặc phát trực tuyến qua Internet. Jetpack Media3 cung cấp một giao diện Player xác định chức năng cơ bản như khả năng phát, tạm dừng, tìm kiếm và hiển thị thông tin track.

- [Dagger Hilt](https://dagger.dev/hilt/) - Hilt là một thư viện tiêm phụ thuộc dành cho Android giúp giảm bớt công đoạn tạo mã tiêm phụ thuộc thủ công trong dự án của bạn.

- [Flow](https://developer.android.com/kotlin/flow) - Flows được xây trên nền tảng coroutines và có thể cung cấp nhiều giá trị. Một flow là một dòng dữ liệu mà có thể được tính toán không đồng bộ.

- [Gradle Kotlin DSL](https://gradle.org/kotlin/) - Nó giúp quản lý phụ thuộc cho tất cả các mô-đun chúng ta có.

