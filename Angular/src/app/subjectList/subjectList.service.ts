import { Injectable } from '@angular/core';
import { Course } from '../model/course.model';
import { HttpClient } from '@angular/common/http';
import { Subject } from '../model/subject.model';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';

const URL = environment.URL + 'api/';

@Injectable()
export class SubjectListService {

  constructor(private http: HttpClient, private router: Router) {

  }

  /* Main */
  getSubject(courseName: string, subjectName: string) {
    const reqUrl = URL + "subjects/" + courseName + "/" + subjectName;
    return this.http.get<Subject>(reqUrl, { withCredentials: true })
  }

  getAllSubjects(courseName: string, page = 0) {
    const reqUrl = URL +'subjects/' + courseName + '/' + 'subjects/all?page=' + page;
    return this.http.get(reqUrl, { withCredentials: true })
  }

  getCourse(courseName: string){
    const reqUrl = URL + "courses/name/" + courseName + "/full";
    return this.http.get<Course>(reqUrl, { withCredentials: true });
  }

  getTeachers(page=0) {
    const reqUrl = URL + "users/all?isStudent=false&page=" + page;
    return this.http.get<any>(reqUrl, { withCredentials: true });
  }

  modifySubject(courseName: string, subject : any): any {
    const reqUrl = URL + "subjects/" + courseName + "/" + subject.internalName;
    return this.http.put<any>(reqUrl, subject, { withCredentials: true });
  }

  createSubject(courseName : string, name : string): any {
    const reqUrl = URL + "subjects/" + courseName + "/subjects?subjectName=" + name;
    return this.http.post<Subject>(reqUrl, {},{ withCredentials: true });
  }

  deleteSubject(courseName : string, subjectName : string): any {
    const reqUrl = URL + "subjects/" + courseName + "/" + subjectName;
    return this.http.delete<Subject>(reqUrl, { withCredentials: true });
  }

}
