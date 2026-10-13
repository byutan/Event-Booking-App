# Event Booking App

## Tech Stack

*   **Ngôn ngữ:** Java 21.0.11
*   **Framework chính:** Spring Boot v4.1.1
*   **Cơ sở dữ liệu:** MySQL Community Edition 8.0.46 
*   **Xác thực & Bảo mật:** Spring Security, JSON Web Tokens 0.13.0
*   **Công cụ & Tiện ích:** Maven, Lombok, Spring Boot Validation
*   **Kiểm thử:** Spring Boot Test, Postman

##  Overview
**Event Booking App** là một nền tảng ứng giúp người dùng tìm kiếm, đăng ký và quản lý các sự kiện.
### Tính năng chính:
* **Dành cho người dùng:** 
  * Tìm kiếm và lọc sự kiện theo danh mục, tọa độ, độ phổ biến và từ khóa.
  * Xem chi tiết sự kiện và đặt, xem vé.
* **Dành cho người tổ chức:** 
  * Tạo, chỉnh sửa và quản lý các sự kiện.

##  Folder Structure
Cấu trúc dự án:

```text
event-booking-app/
├── .idea/                                        # Thư mục cấu hình của IDE IntelliJ IDEA
├── .mvn/                                         # Thư mục cấu hình của Maven wrapper
└── src/                                          # Thư mục chứa mã nguồn chính
    ├── main/                                     # Mã nguồn thực thi của ứng dụng
    │   ├── java/
    │   │   └── jp.co.frux.crud.eventbookingapp/
    │   │       ├── controller/                   # Chứa các REST API controllers xử lý request
    │   │       ├── dto/                          # Data Transfer Objects, dùng để truyền dữ liệu giữa các layer
    │   │       ├── entity/                       # Định nghĩa các entities mapping với cơ sở dữ liệu
    │   │       ├── exception/                    # Xử lý các ngoại lệ của ứng dụng
    │   │       ├── repository/                   # Giao tiếp với cơ sở dữ liệu 
    │   │       ├── security/                     # Cấu hình bảo mật, phân quyền 
    │   │       ├── service/                      # Chứa logic nghiệp vụ của ứng dụng
    │   │       └── EventBookingAppApplication.java 
    │   └── resources/     
    │       ├── static/      
    │       ├── templates/   
    │       └── application.properties            # File cấu hình chính của Spring Boot 
    └── test/              
        └── java/
            └── jp.co.frux.crud.eventbookingapp/
                ├── controller/                   # Test cho controllers
                ├── service.impl/                 # Test cho service implementations
                └── EventBookingAppApplicationTests.java
```

##  Running
### Cơ sở dữ liệu:
*   **Cài đặt Hệ quản trị cơ sở dữ liệu MySQL Community bản 8.0.46:** [MySQL Community Downloads](https://dev.mysql.com/downloads/installer/).
*   **Kiểm tra phiên bản MySQL:** Sau khi cài đặt MySQL, tạo tài khoản và đặt mật khẩu, kiểm tra phiên bản như hình bằng ```MySQL 8.0 Command Line Center```. <img width="955" height="282" alt="image" src="https://github.com/user-attachments/assets/834f6331-9826-495d-ab8e-bd11c1a4f9c2" />
*   **Tạo Connection trên MySQL Workbench 8.0 CE:**
    *   Chọn như hình để khởi tạo Connection. <img width="1917" height="1017" alt="image" src="https://github.com/user-attachments/assets/28d8153a-448b-450b-af76-d2b3a226c6ad" />
    *   Điền *event-booking-app* vào ```Connection Name``` và nhấn ```Test Connection```.<img width="977" height="607" alt="image" src="https://github.com/user-attachments/assets/708e3a0e-c25f-4875-8172-c0c9e2f0e91f" />
    *   Điền *mật khẩu* của tài khoản MySQL, chọn ```Save password in vault``` và nhấn ```OK```:<img width="977" height="607" alt="image" src="https://github.com/user-attachments/assets/0e843ca9-5075-4211-87d6-33aa54c5b837" />
    *   Nhận thông báo ```thành công``` và nhấn ```OK```:<img width="987" height="620" alt="image" src="https://github.com/user-attachments/assets/538e2537-f626-4419-b1db-f16ae2b680e1" />
    *   Nhấn ```OK``` và connection sẽ được tạo: <img width="975" height="617" alt="image" src="https://github.com/user-attachments/assets/2f2cfdeb-f1b3-45b0-9c60-1ddc6e4586bb" />
*   **Tạo Schema:**
    *   Chọn Connection ```event-booking-app```. <img width="1917" height="1020" alt="image" src="https://github.com/user-attachments/assets/d8022bc0-6ead-4ae0-a2f0-13397a1049be" />
    *   Copy nội dung file ```event_booking_app_schema.sql```: [Event booking App schema](https://drive.google.com/file/d/1aRisdnjjplIG4QNiX0bv1px0vntKvhvL/view?usp=sharing)
    *   Dán nội dung file ```event_booking_app_schema.sql``` vào ```tab Query``` và nhấn vào chỗ chỉ định: <img width="1917" height="1020" alt="image" src="https://github.com/user-attachments/assets/191cf467-7d34-4814-957e-df56484348d6" />
    *   Kiểm tra kết quả chạy như hình: <img width="1915" height="483" alt="image" src="https://github.com/user-attachments/assets/7b610076-2754-4245-9725-ffd05ee6bd9d" />
*   **Kiểm tra services MySQL:**
    *   Sau khi nhấn tổ hợp ```Window + R``` hoặc ```phím Window + điền "Run" vào ô tìm kiếm```, điền *services.msc*: <img width="450" height="267" alt="image" src="https://github.com/user-attachments/assets/bbc29357-16f3-466d-98e0-60b99ddf935e" />
    *   Kiểm tra trạng thái ```Running```, nếu không có, hãy ```chuột phải + start``` service MySQL: <img width="632" height="36" alt="image" src="https://github.com/user-attachments/assets/11680171-e5b5-4899-8c31-a6a14ab3d886" />

### Event-Booking-App:
*   **Tải Java 21.0.11:** [Java 21.0.11 Downloads](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
*   **Tải IDE IntelliJ Community Edition:** [IntelliJ IDEA Downloads](https://www.jetbrains.com/idea/download/?section=windows)
*   **Clone Repo về Local**
    *   Dán ```https://github.com/byutan/Event-Booking-App.git``` và chọn đường dẫn thư mục phù hợp để clone repo về. <img width="1000" height="811" alt="image" src="https://github.com/user-attachments/assets/ae5c49de-d6fb-438e-b483-ae52266a6a36" />
    *   Chọn ```Trust Project```. <img width="787" height="315" alt="image" src="https://github.com/user-attachments/assets/d1dd9eb1-e426-44da-b981-b0b7fdd9190e" />
*   **Cấu hình CSDL của Project**
    *   Vào ```.\src\main\resources\application.properties```, sửa lại cấu hình thành *tên tài khoản* và *mật khẩu* MySQL local. <img width="1917" height="1021" alt="image" src="https://github.com/user-attachments/assets/6c86ad00-5a8c-4bf9-a877-3381b129e88d" />
*   **Cấu hình lệnh chạy của Project**
    *   Chọn mục ```Edit Configuration```. <img width="1916" height="297" alt="image" src="https://github.com/user-attachments/assets/e3e19a4b-07d2-473e-a959-ee583cc2f808" /> 
    *   Sau khi chọn ```Add new run configuration``` thì Chọn **Spring Boot**. <img width="1212" height="847" alt="image" src="https://github.com/user-attachments/assets/592d6605-5ffa-4bda-8142-4010d0ff94f2" />
    *   Đặt tên ```Event-Booking-App```cho lệnh Run, chọn ```Java 21```, chọn ```EventBookingAppApplication```, nhấn ```OK``` và nhấn ```OK``` lần cuối để hoàn thành cấu hình lệnh chạy. <img width="1656" height="862" alt="image" src="https://github.com/user-attachments/assets/f9e4e73f-1945-497f-811a-9b720f43f11f" />
*   **Chạy Project**
    *   Nhấn vào chỗ chỉ định để thực hiện lệnh chạy: <img width="1917" height="385" alt="image" src="https://github.com/user-attachments/assets/e9bdfcf0-1d31-4f77-a379-caebf860ff3f" />
    *   Chạy **thành công**: <img width="1917" height="985" alt="image" src="https://github.com/user-attachments/assets/14214b35-3454-4cce-8e54-c0bfe6047329" />
    
### Kiểm thử API bằng Postman:
*   **Tải Postman:**  [Postman Downloads](https://www.postman.com/downloads/)
*   **Import Collection**
    *   Copy nội dung file ```event-booking-app.postman_collection.json```: [Event booking App API Collection](https://drive.google.com/file/d/13KK0Hw8fM0ynkJbQwTKrE14q_hLQsClj/view?usp=sharing)
    *   Nhấn vào nút ```Use resources or import```. <img width="1917" height="1025" alt="image" src="https://github.com/user-attachments/assets/1bc5b5c2-c4dc-4e44-aa2b-20e4d0798763" />
    *   Dán nội dung file ```event-booking-app.postman_collection.json``` và nhấn ```Enter``` để import <img width="1062" height="720" alt="image" src="https://github.com/user-attachments/assets/7caa0eb4-d5ce-42f8-91ae-0a9f0b2d393d" />
    *   Import thành công. <img width="1917" height="532" alt="image" src="https://github.com/user-attachments/assets/03f2b48a-a26b-4e5a-b23a-52e5503185e1" />
*   **Kiểm thử Postman**
    * API POST /api/auth/register: <img width="1917" height="987" alt="image" src="https://github.com/user-attachments/assets/3d8dcd8d-d891-449b-9f0b-9db74a438c32" />


      
