import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {User} from "../model/user";
import {BehaviorSubject, Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly user$ = new BehaviorSubject<User | null>(null)

  get user(): Observable<User | null> {
    return this.user$.asObservable()
  }

  constructor(private http: HttpClient) {
    // this.user$.next({username: "Dummy"})
  }

  isLoggedIn(): boolean {
    return !!this.user$.value
  }

  login(credentials: { email: string, password: string }): void {
    this.user$.next({username: credentials.email})
    //TODO login on server
    console.log('login');
  }

  register(credentials: { email: string, username: string, password: string }): void {
    this.user$.next({username: credentials.username})
    //TODO register on server
    console.log('register');
  }

  logout(): void {
    this.user$.next(null)
    //TODO logout on server
    console.log('logout');
  }

  resetPassword(email: string) {
    //TODO
    console.log('reset PW');
  }
}
