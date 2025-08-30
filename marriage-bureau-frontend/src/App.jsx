import React from 'react';
import './App.css';
import LoginForm from './components/LoginForm.jsx'; // <-- New import
// import RegistrationForm from './components/RegistrationForm.jsx'; // <-- Comment out or remove this line

function App() {
  return (
    <div className="App">
      <LoginForm /> {/* <-- Use the new component */}
    </div>
  );
}

export default App;