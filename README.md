# 🗓️ StudyPlanner – Java Task Management App

A personal task manager built in **Java Swing**, following the **MVC design pattern**. Users can sign up, log in, and manage tasks with deadlines and priorities. Tasks are persisted locally per user. Includes features like sorting, editing, and completion tracking.

---

## 🌟 Features

### 🔐 **User Authentication**
  - Sign up and log in with unique user credentials
  - Each user has their own saved task list (stored locally)

### 📝 **Add & Edit Tasks**
  - Add tasks with a title, description, priority level, and deadline
  - Edit any field of an existing task
  - Deadline input uses a calendar date picker (`JDateChooser`)

### ✅ **Complete Tasks**
  - Check off tasks to mark them as complete
  - Completed tasks are highlighted in green

### 🗑️ **Delete Tasks**
  - Remove tasks permanently from your list

### 🧭 **Task List Dashboard**
  - Shows tasks as lists.
  - Each task is color-coded according to its priority level.
  - Easily toggle between:
    - Pending Tasks
    - Overdue Tasks
    - Completed Tasks
    - All Tasks
   
### 🔁 **Sort Task List**
  - Sort by **priority level** (High → Low)
  - Sort by **deadline** (Sooner → Later)
 
### 📅 **Calendar Dashboard** 
  - Visual calendar interface as the default home view after login
  - 📅 Week View: Shows detailed and highlighted task titles & descriptions in a 7-day layout (default home after login)
    - Click on the highlighted tasks to view details, or complete or delete the task.
  - 🗓️ Month View: Shows a full month with task dots per day
    - Click on the dot to view task details, or complete or delete the task.
  - Click the side menu of a day to view, complete, or show details of tasks due on that date
  - Navigate between weeks or months.
 
### 📊 **Statistics Dashboard** 
  - Visual insights into your productivity:
    - Total Tasks
    - Completed Today/This Week
    - Pending / Overdue
    - Completion Rate with Progress Bar
      
  - Interactive chart panel with toggles:
    - 🌦 Task Forecast (Stacked Bar Chart)
    - 📋 Task Distribution (Pie Chart)
    - 📉 Overdue History (Line Chart – 7/14/30 day range)
  - Uses JFreeChart for high-quality, customizable charts

### 🔍 **Search & Filter Tasks** 
  - Keyword-based search (title, description, or status)
  - Filter tasks by:
    - **Priority level**
    - **Status**: Completed, Overdue, Due today, Due tomorrow, Due this week, Due next week, Due in 3/14/30 days
  - Combine search and filters for precise results
  - Results are displayed dynamically in a scrollable list

### 💾 **Local Persistence**
  - Task data is serialized and saved to a user-specific `.ser` file
  - On login, your previous tasks are restored

### 🚪 **Log Out**
  - Securely log out from the session
  - Automatically saves tasks before exiting to the login screen

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

