export const Login = async (username: string, password: string) => {
    try {
        const apiUrl = import.meta.env.VITE_API_URL || "http://localhost:8080";

        const response = await fetch(`${apiUrl}/api/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        });
        if (!response.ok) {
            throw new Error('Login gagal, periksa kembali username dan password');
        }
        const data = await response.json();
        localStorage.setItem('token', data.token); // Simpan token
        return true;
    } catch (error) {
        console.error("Login Error:", error);
        return false;
    }
};