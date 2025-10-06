
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import DashboardAdmin from './pages/DashboardAdmin';

function App() {
  return (
    <Routes>
      <Route path="/dashboardAdmin" element={<DashboardAdmin />} />
    </Routes>
  );
}

export default App;
