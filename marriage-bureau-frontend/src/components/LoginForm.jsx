// File: src/components/LoginForm.jsx
import React, { useState } from 'react';

const LoginForm = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [isSuccess, setIsSuccess] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  // Use a relative path now that Vite is handling the proxy.
  const API_BASE_URL = '/api/auth';

  const handleSubmit = async (event) => {
    event.preventDefault();
    setMessage('');
    setIsLoading(true);

    try {
      const response = await fetch(`${API_BASE_URL}/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password }),
      });

      const data = await response.json();

      if (response.ok) {
        setIsSuccess(true);
        setMessage('Login successful! Welcome back.');
        localStorage.setItem('jwtToken', data.accessToken);
        console.log('Login successful, token stored:', data.accessToken);
      } else {
        setIsSuccess(false);
        setMessage(data.message || 'Login failed. Please check your credentials.');
      }
    } catch (error) {
      setIsSuccess(false);
      setMessage('Network error: Could not connect to the server. Please ensure the backend is running and the URL is correct.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div style={formContainerStyle}>
      <h2 style={headingStyle}>Login to Your Account</h2>
      <form onSubmit={handleSubmit} style={formStyle}>
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
        <button style={buttonStyle} type="submit" disabled={isLoading}>
          {isLoading ? 'Logging in...' : 'Login'}
        </button>
      </form>
      {message && (
        <div style={{ ...messageBoxStyle, backgroundColor: isSuccess ? '#d4edda' : '#f8d7da', color: isSuccess ? '#155724' : '#721c24' }}>
          {message}
        </div>
      )}
    </div>
  );
};
// --- Styles (these can be moved to a separate CSS file later) ---
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

export default LoginForm;