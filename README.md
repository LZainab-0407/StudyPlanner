# StudyPlanner – Java Task Management App

A personal task and study planner with an intuitive calendar interface, visual statistics, and persistent cloud storage. Users can sign up, log in, and manage tasks with deadlines and priorities. Built using Java Swing and backed by Google Sheets for scalable, multi-user task tracking.

---

## Features

### **Add & Edit Tasks**
  - Add tasks with a title, description, priority level, and deadline
  - Edit any field of an existing task
  - Deadline input uses a calendar date picker (`JDateChooser`)

### **Complete Tasks**
  - Check off tasks to mark them as complete
  - Completed tasks are highlighted in green

### **Delete Tasks**
  - Remove tasks permanently from your list

### **Task List Dashboard**
  - Shows tasks as lists.
  - Each task is color-coded according to its priority level.
  - Easily toggle between:
    - Pending Tasks
    - Overdue Tasks
    - Completed Tasks
    - All Tasks
   
### **Sort Task List**
  - Sort by **priority level** (High → Low)
  - Sort by **deadline** (Sooner → Later)
 
### **Calendar Dashboard** 
  - Visual calendar interface as the default home view after login
  - Week View: Shows detailed and highlighted task titles & descriptions in a 7-day layout (default home after login)
    - Click on the highlighted tasks to view details, or complete or delete the task.
  - Month View: Shows a full month with task dots per day
    - Click on the dot to view task details, or complete or delete the task.
  - Click the side menu of a day to view, complete, or show details of tasks due on that date
  - Navigate between weeks or months.
 
### **Statistics Dashboard** 
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

### **Search & Filter Tasks** 
  - Keyword-based search (title, description, or status)
  - Filter tasks by:
    - **Priority level**
    - **Status**: Completed, Overdue, Due today, Due tomorrow, Due this week, Due next week, Due in 3/14/30 days
  - Combine search and filters for precise results
  - Results are displayed dynamically in a scrollable list

### **Cloud Sync** 
  - Tasks are saved to a centralized Google Sheet, allowing multiple users to manage their own data across devices.

### **User Authentication** 
  - Supports signup and login with credentials stored securely in the cloud.

### **User Management in the Cloud**
  - User credentials (username + password) are stored in a **Users** sheet.
  - Each task includes a `username` field, so user data is separated securely.
  - During signup, the app checks the **Users** sheet to prevent duplicate usernames.
  - During login, credentials are validated against the **Users** sheet.
  
### **Log Out**
  - Securely log out from the session
  - Automatically saves tasks before exiting to the login screen

---

## Saving Strategy (Performance Optimization)
To reduce API calls and improve performance:
- Tasks are only saved to Google Sheets:
  - When the user clicks a "Save Now" button (optional)
  - On logout
  - On closing the app
- An internal `taskListModified` flag tracks unsaved changes.

  ---

## Technologies Used

- **Java Swing** – for GUI
- **MVC Architecture** – to separate model, view, and control logic
- **JCalendar Library** – for date picking (via `JDateChooser`)
- **Custom Icons & Styling** – from https://icons8.com/icons Clean and modern UI
- **Google Sheets API (OAuth 2.0 authentication)**
- **Maven for dependency management**

---

## Project Structure

-/src
  - ├── models/ # Task, User classes
  - ├── views/ # GUI panels & frames
  - ├── controllers/ # Event and logic handlers
  - ├── data/ # UserDataManager, TaskManager
  - └── Main.java # Entry point

---

## Getting Started
Follow these steps to clone, run, and try the StudyPlanner app with full functionality (including Google Sheets cloud sync).
1. Prerequisites
    -  Java 21+
    -  Eclipse IDE for Java Developers (or any IDE that supports Maven)
    -  Git
    -  A Google Account (for using the Sheets integration)

2. Clone the Repository

3. Google Sheets API Setup
    - The app uses Google Sheets as a cloud backend. To access Sheets via the API, contact me to be added as a test user. This is required because the app is still in Google OAuth testing mode, so send your Google email address to: labibah.mimm@gmail.com.
    - Once added as a test user, run the app. A browser window will open the Google Authorization screen
    - Sign in and allow access. A token will be saved locally for future sessions.

4. Config and Credential Files
    - Once you are added as a test user, I can securely share this file with you. Save it in the following path: src/main/resources/credentials.json

5. Run the App
    - Sign up as a new user and start using the StudyPlanner!

---

## Author
Labibah Zainab
- 📚 Computer Science @ Iowa State University
- 💻 Java Enthusiast | Math Minor | GitHub Learner
- 🔗 GitHub: LZainab-0407

 ---

## License
This project is open-source for educational and portfolio use.
Feel free to contribute or suggest features by opening issues or pull requests! 🎉

