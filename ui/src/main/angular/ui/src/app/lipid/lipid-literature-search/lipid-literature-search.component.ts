import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { NgForm } from '@angular/forms';
import { SolrSearchResponse, SolrsearchService } from '../../_services/solrsearch.service';

@Component({
  selector: 'app-lipid-literature-search',
  templateUrl: './lipid-literature-search.component.html',
  styleUrls: ['./lipid-literature-search.component.css']
})
export class LipidLiteratureSearchComponent implements OnInit, OnChanges {

  solrResponse: SolrSearchResponse;

  @Input()
  query: string;

  lastQuery: string;

  ndocs = 0;

  docKeys = ['title', 'author', 'doi', 'keywords', 'subject', 'date', 'created', 'creation_date'];

  constructor(private solrsearchService: SolrsearchService) { }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges) {
    this.doQuery(changes['query'].currentValue).then(response => {
      this.solrResponse = response;
      this.lastQuery = this.query;
      this.ndocs = response.response.numFound;
    });
  }

  onSubmit(f: NgForm) {
    this.doQuery(this.query).then(response => {
      this.solrResponse = response;
      this.lastQuery = this.query;
      this.ndocs = response.response.numFound;
    });
  }

  private doQuery(query: string): Promise<SolrSearchResponse> {
    if ( this.query === null || this.query === '' ) {
      return this.solrsearchService.search('*:*');
    }
    return this.solrsearchService.search(this.query);
  }

}
