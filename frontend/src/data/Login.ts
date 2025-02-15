export const Login = async (username: string, password: string) => {
    try{
        const response = await fetch('http://localhost:8000/api/auth/login/', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({username, password})
        });
        if (!response.ok) {
            throw new Error('Login gagal, periksa kembali username dan password');
        }
       const data = await response.json();
       localStorage.setItem('token', data.token);//simpan token
       return true; 
    } catch (error) {   
     console.error("Login Error:",error);
     return false;
    }
};