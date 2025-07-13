# 🗓️ StudyPlanner – Java Task Management App

A personal task and study planner with an intuitive calendar interface, visual statistics, and persistent cloud storage. Users can sign up, log in, and manage tasks with deadlines and priorities. Built using Java Swing and backed by Google Sheets for scalable, multi-user task tracking.

---

## 🌟 Features

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

### ☁️ **Cloud Sync** 
  - Tasks are saved to a centralized Google Sheet, allowing multiple users to manage their own data across devices.

### 🔐 **User Authentication** 
  - Supports signup and login with credentials stored securely in the cloud.

### 🔐 **User Management in the Cloud**
  - User credentials (username + password) are stored in a **Users** sheet.
  - Each task includes a `username` field, so user data is separated securely.
  - During signup, the app checks the **Users** sheet to prevent duplicate usernames.
  - During login, credentials are validated against the **Users** sheet.
  
### 🚪 **Log Out**
  - Securely log out from the session
  - Automatically saves tasks before exiting to the login screen

---

## 💾 Saving Strategy (Performance Optimization)
To reduce API calls and improve performance:
- Tasks are only saved to Google Sheets:
  - When the user clicks a "Save Now" button (optional)
  - On logout
  - On closing the app
- An internal `taskListModified` flag tracks unsaved changes.

  ---

## 🧠 Technologies Used

- **Java Swing** – for GUI
- **MVC Architecture** – to separate model, view, and control logic
- **JCalendar Library** – for date picking (via `JDateChooser`)
- **Custom Icons & Styling** – from https://icons8.com/icons Clean and modern UI
- **Google Sheets API (OAuth 2.0 authentication)**
- **Maven for dependency management**

---
## 📁 To run this project locally:
1. Clone the repo
2. Ensure your `credentials.json` is in `src/main/resources`
3. Launch the app from your `Main.java` or the GUI launcher
4. On first run, the app will prompt for Google OAuth login to authorize access
6. Sign up and start managing your tasks!

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

