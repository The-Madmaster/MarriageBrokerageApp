import React, { useState } from 'react';

const RegistrationForm = () => {
  // State to hold the user's input
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [contactNumber, setContactNumber] = useState('');
  
  // New state for managing UI feedback
  const [message, setMessage] = useState('');
  const [isSuccess, setIsSuccess] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

   // ⭐ Updated API_BASE_URL with your new Codespaces URL
  //const API_BASE_URL = 'https://upgraded-yodel-pw6qp64w5qrf9657-8080.app.github.dev/api/auth';
  const API_BASE_URL ='/api/auth';
 // const API_BASE_URL = 'http://localhost:8080/api/auth'; // Replace with your backend URL if different
  // Note: For Codespaces, you might need to use the full URL like 'https://<your-codespace-url>-8080.app.github.dev/api/auth'

  const handleSubmit = async (event) => {
    event.preventDefault(); 
    setMessage('');
    setIsLoading(true);

    const registrationData = { email, password, contactNumber };

    try {
      const response = await fetch(`${API_BASE_URL}/register`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(registrationData),
      });

      const data = await response.json();
      
      if (response.ok) {
        // Registration successful
        setIsSuccess(true);
        setMessage('Registration successful! Please log in with your new account.');
        // Clear the form
        setEmail('');
        setPassword('');
        setContactNumber('');
        
        // Optional: Store the token if the backend sends one immediately
        if (data.accessToken) {
          localStorage.setItem('jwtToken', data.accessToken);
        }
        
      } else {
        // Registration failed (e.g., email already exists, bad request)
        setIsSuccess(false);
        setMessage(data.message || 'Registration failed. Please try again.');
      }
    } catch (error) {
      // Handle network errors
      setIsSuccess(false);
      setMessage('Network error: Could not connect to the server.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div style={formContainerStyle}>
      <h2 style={headingStyle}>Create Your Broker Account</h2>
      <form onSubmit={handleSubmit} style={formStyle}>
        
        {/* Email Input */}
        <div style={inputGroupStyle}>
          <label style={labelStyle} htmlFor="email">Email:</label>
          <input
            style={inputStyle}
            type="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        
        {/* Password Input */}
        <div style={inputGroupStyle}>
          <label style={labelStyle} htmlFor="password">Password:</label>
          <input
            style={inputStyle}
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        
        {/* Contact Number Input */}
        <div style={inputGroupStyle}>
          <label style={labelStyle} htmlFor="contactNumber">Contact Number:</label>
          <input
            style={inputStyle}
            type="text"
            id="contactNumber"
            value={contactNumber}
            onChange={(e) => setContactNumber(e.target.value)}
            required
          />
        </div>
        
        {/* Submit Button */}
        <button style={buttonStyle} type="submit" disabled={isLoading}>
          {isLoading ? 'Registering...' : 'Register'}
        </button>
      </form>

      {/* Message Display Area */}
      {message && (
        <div style={{ ...messageBoxStyle, backgroundColor: isSuccess ? '#d4edda' : '#f8d7da', color: isSuccess ? '#155724' : '#721c24' }}>
          {message}
        </div>
      )}
    </div>
  );
};

// ... (remaining styles from before)
const formContainerStyle = {
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  justifyContent: 'center',
  minHeight: '100vh',
  backgroundColor: '#f0f2f5',
  fontFamily: 'Arial, sans-serif'
};

const headingStyle = {
  color: '#333',
  marginBottom: '20px'
};

const formStyle = {
  backgroundColor: '#fff',
  padding: '40px',
  borderRadius: '8px',
  boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
  width: '300px',
  display: 'flex',
  flexDirection: 'column',
  gap: '15px'
};

const inputGroupStyle = {
  display: 'flex',
  flexDirection: 'column'
};

const labelStyle = {
  marginBottom: '5px',
  fontWeight: 'bold',
  color: '#555'
};

const inputStyle = {
  padding: '10px',
  borderRadius: '4px',
  border: '1px solid #ccc',
  fontSize: '16px'
};

const buttonStyle = {
  padding: '12px',
  borderRadius: '4px',
  border: 'none',
  backgroundColor: '#007bff',
  color: '#fff',
  fontSize: '16px',
  cursor: 'pointer',
  marginTop: '10px'
};

const messageBoxStyle = {
  marginTop: '20px',
  padding: '10px',
  borderRadius: '4px',
  textAlign: 'center',
  width: '300px'
};

export default RegistrationForm;