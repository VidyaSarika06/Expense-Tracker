import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';

// ng2-charts v4 requires Chart.js components to be registered globally.
// Without this, all chart canvases render blank.
import {
  Chart,
  ArcElement,
  BarElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend,
  Title,
  PieController,
  BarController
} from 'chart.js';

Chart.register(
  ArcElement,
  BarElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend,
  Title,
  PieController,
  BarController
);

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
