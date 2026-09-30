export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  email: string;
  role: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
  role: string;
}

export interface Category {
  id?: number;
  name: string;
  description?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface Transaction {
  id?: number;
  amount: number;
  type: 'INCOME' | 'EXPENSE';
  date: string;
  note?: string;
  categoryId: number;
  categoryName?: string;
}

export interface Budget {
  id?: number;
  month: string;
  limitAmount: number;
  categoryId: number;
  categoryName?: string;
}

export interface CategoryExpense {
  category: string;
  amount: number;
}

export interface MonthlyTrend {
  month: string;
  income: number;
  expense: number;
}

export interface BudgetAlert {
  category: string;
  budget: number;
  spent: number;
  overrun: number;
}

export interface MonthlySummary {
  totalIncome: number;
  totalExpense: number;
  balance: number;
  expenseByCategory: CategoryExpense[];
  monthlyTrend: MonthlyTrend[];
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
