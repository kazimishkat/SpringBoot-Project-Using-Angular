import * as CryptoJS from 'crypto-js';
// Import the standalone SECRET alongside the environment object
import { environment, SECRET } from '../../environments/environment';

export const CryptoUtil = {
  encrypt(value: string): string {
    if (!value) return '';
    // Use the variable directly instead of environment.SECRET
    return CryptoJS.AES.encrypt(value, SECRET).toString();
  },

  decrypt(cipher: string): string | null {
    if (!cipher) return null;
    try {
      // Use the variable directly instead of environment.SECRET
      const bytes = CryptoJS.AES.decrypt(cipher, SECRET);
      const result = bytes.toString(CryptoJS.enc.Utf8);
      return result || null;
    } catch {
      return null;
    }
  }
};