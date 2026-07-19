import { Injectable } from '@angular/core';
import { LoginResponse } from '../models/auth.model';
import { CryptoUtil } from '../utils/crypto.util';


// Unique storage keys tailored exactly for your SmartPharmacy domain
export const KEYS = {
  TOKEN: 'sp_token',
  USER: 'sp_user',
  BRANCH: 'sp_branch'
};

@Injectable({
  providedIn: 'root',
})
export class StorageService {

  // ── Session Write ─────────────────────────────────────

  /**
   * Saves the user authentication payload securely.
   * Encrypts the raw JWT string and the full metadata profile details.
   */
  saveSession(data: LoginResponse): void {
    localStorage.setItem(
      KEYS.TOKEN,
      CryptoUtil.encrypt(data.token)
    );
    localStorage.setItem(
      KEYS.USER,
      CryptoUtil.encrypt(JSON.stringify(data))
    );
    
    // Explicitly cache branch contextual information if the user is bound to one
    if (data.branchId) {
      const branchContext = { id: data.branchId, name: data.branchName };
      this.saveData(KEYS.BRANCH, branchContext);
    }
  }

  // ── Session Read ──────────────────────────────────────

  /**
   * Retrieves and decrypts the active Bearer token value.
   */
  getToken(): string | null {
    const raw = localStorage.getItem(KEYS.TOKEN);
    return raw ? CryptoUtil.decrypt(raw) : null;
  }

  /**
   * Retrieves and parses the current user identity matrix data profile.
   */
  getUser(): LoginResponse | null {
    const raw = localStorage.getItem(KEYS.USER);
    if (!raw) return null;
    
    try {
      const json = CryptoUtil.decrypt(raw);
      return json ? JSON.parse(json) as LoginResponse : null;
    } catch (error) {
      console.error('Failed parsing security context structural values:', error);
      return null;
    }
  }

  /**
   * Directly extracts the UserRole matching your Spring Boot enum patterns.
   */
  getRole(): string | null {
    return this.getUser()?.role ?? null;
  }

  /**
   * Quick boolean confirmation indicating presence of an active security token.
   */
  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  /**
   * Fetches specific cached pharmacy operational branch mappings if available.
   */
  getBranchContext(): { id: number; name: string } | null {
    return this.getData<{ id: number; name: string }>(KEYS.BRANCH);
  }

  // ── Session Destruction ───────────────────────────────

  /**
   * purges all framework and application credentials during operational exit tasks.
   */
  clearSession(): void {
    Object.values(KEYS).forEach(k => localStorage.removeItem(k));
  }

  // ── Generic Security Wrappers ─────────────────────────

  /**
   * Encrypts and writes generic data blobs into window storage.
   */
  saveData(key: string, data: any): void {
    localStorage.setItem(
      key,
      CryptoUtil.encrypt(JSON.stringify(data))
    );
  }

  /**
   * Decrypts and dynamically parses a complex data type structure safely.
   */
  getData<T>(key: string): T | null {
    const raw = localStorage.getItem(key);
    if (!raw) return null;

    try {
      const json = CryptoUtil.decrypt(raw);
      return json ? (JSON.parse(json) as T) : null;
    } catch (error) {
      console.error(`Failed loading dynamic data object map for key [${key}]:`, error);
      return null;
    }
  }

  /**
   * Drops specific key identities without modifying active adjacent memory spaces.
   */
  removeData(key: string): void {
    localStorage.removeItem(key);
  }
}