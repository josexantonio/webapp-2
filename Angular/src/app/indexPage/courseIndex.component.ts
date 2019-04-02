import { Component, OnInit, Input } from '@angular/core';
import { Course } from '../model/course.model';
import { environment } from '../../environments/environment';


@Component({
  selector: 'course-index-component',
  templateUrl: './courseIndex.component.html',
  styleUrls: ['../../assets/css/style.css']
})

export class CourseIndexComponent {

  @Input()
  public course: Course;

  public imageURL: string = environment.URL;

  constructor() {
  }


}