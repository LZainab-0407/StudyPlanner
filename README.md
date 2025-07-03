# 🗓️ StudyPlanner – Java Task Management App

A personal task manager built in **Java Swing**, following the **MVC design pattern**. Users can sign up, log in, and manage tasks with deadlines and priorities. Tasks are persisted locally per user. Includes features like sorting, editing, and completion tracking.

---

## 🌟 Features

- 🔐 **User Authentication**
  - Sign up and log in with unique user credentials
  - Each user's task list is securely saved to disk

- 📝 **Add & Edit Tasks**
  - Add tasks with a title, description, priority level, and deadline
  - Edit any field of an existing task
  - Deadline input uses a calendar date picker (`JDateChooser`)

- ✅ **Complete Tasks**
  - Check off tasks to mark them as complete
  - Completed tasks are highlighted in green

- 🗑️ **Delete Tasks**
  - Remove tasks permanently from your list
 
- 📅 **Calendar Dashboard** 
  - Visual calendar interface as the default home view after login
  - Each day has highlighted dots, each representing a task
  - The highlight color depends on priority
  - Click on the dot to see or edit the task
  - Click the side menu of a day to view tasks due on that date

- 🔁 **Sort Task List**
  - Sort by **priority level** (High → Low)
  - Sort by **deadline** (Sooner → Later)

-🔍 **Search & Filter Tasks** 
  - Keyword-based search (title, description, or status)
  - Filter tasks by:
    - **Priority level**
    - **Status**: Overdue, Due Today, Due in 7/14/30 days
  - Combine search and filters for precise results
  - Results are displayed dynamically in a scrollable list

- 💾 **Local Persistence**
  - Task data is serialized and saved to a user-specific `.ser` file
  - On login, your previous tasks are restored

- 🚪 **Log Out**
  - Securely log out from the session
  - Automatically saves tasks before exiting to login screen

---

## 🧠 Technologies Used

- **Java Swing** – for GUI
- **MVC Architecture** – to separate model, view, and control logic
- **JCalendar Library** – for date picking (via `JDateChooser`)
- **Java Serialization** – for persistent storage
- **Custom Icons & Styling** – from https://icons8.com/icons Clean and modern UI

---

## 📁 How to Run the Project
- Clone or download the repository
- Open the project in Eclipse or VS Code
- Make sure the data/ folder exists or will be auto-created
- Run Main.java
- Sign up and start managing your tasks!

---

## 📁 Project Structure

-/src
  - ├── models/ # Task, User classes
  - ├── views/ # GUI panels & frames
  - ├── controllers/ # Event and logic handlers
  - ├── data/ # UserDataManager, TaskManager
  - └── Main.java # Entry point

---

## Author
Labibah Zainab
- 📚 Computer Science @ Iowa State University
- 💻 Java Enthusiast | Math Minor | GitHub Learner
- 🔗 GitHub: LZainab-0407

 ---

## 📜 License
This project is open-source for educational and portfolio use.
Feel free to contribute or suggest features by opening issues or pull requests! 🎉

