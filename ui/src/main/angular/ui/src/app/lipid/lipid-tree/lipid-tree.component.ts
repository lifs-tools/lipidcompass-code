import { Component, OnInit, Input } from '@angular/core';
import {TreeModule} from 'primeng/tree';
import {TreeNode} from 'primeng/api';

import { Subscription } from 'rxjs';
import { SelectionService } from '../../_services/selection.service';
import { LipidNodeService } from '../../_services/lipidnode.service';

export class LipidCategoryNode implements TreeNode {
  constructor() {}
}

@Component({
  selector: 'app-lipid-tree',
  templateUrl: './lipid-tree.component.html',
  styleUrls: ['./lipid-tree.component.css']
})
export class LipidTreeComponent implements OnInit {
  initialLipids: TreeNode[] = Array<TreeNode>();
  lipids: TreeNode[] = Array<TreeNode>();
  children: TreeNode[] = Array<TreeNode>();
  selectionPath: TreeNode[] = Array<TreeNode>();
  parentNode: TreeNode;

  @Input()
  loading = false;

  @Input()
  disabled = false;

  subscription: Subscription;

  constructor(
    private lipidNodeService: LipidNodeService,
    private selectionService: SelectionService<TreeNode>
  ) {}

  ngOnInit() {
    this.subscription = this.lipidNodeService.backendAvailable$.subscribe(
      res => {
        if (res) {
          this.disabled = false;
        } else {
          this.disabled = true;
        }
      }
    );
    if (this.lipids.length === 0) {
      this.load();
    }
  }

  private load(): void {
    this.loading = true;
    this.lipidNodeService
      .categories()
      .subscribe(lipids => {
        this.initialLipids = lipids;
        console.debug(JSON.stringify(this.initialLipids));
        this.lipids = lipids;
        this.loading = false;
      });
  }

  nodeSelect(event: any) {
    if (event && event.node) {
      this.selectionService.select(<TreeNode>event.node);
      if (this.parentNode == null) {
        this.parentNode = event.node;
      }
      // console.debug('Received node via event: ' + JSON.stringify(event.node));
      // if (this.selectionService) {
      //     // this.selectionService.select(event.node);
      // } else {
      //     // console.debug('Selection service is null or undefined!');
      // }
    }
  }

  nodeClick(node: TreeNode) {
    this.selectionService.select(node);
  }

  parentClick(node: TreeNode) {
    var otherNode = this.selectionPath.pop();
    if (this.selectionPath.length === 0) {
      this.parentNode = null;
      this.lipids = this.initialLipids;
      this.children = [];
    } else if (otherNode) {
      this.selectionService.select(otherNode);
      this.lipids = Array<TreeNode>(otherNode);
      if (otherNode.children && otherNode.children.length > 0) {
        this.children = otherNode.children;
      } else {
        this.children = [];
      }
    }

    // this.lipidNodeService.childrenFor(node).subscribe(children => {
    //   // console.debug('Received children: ' + JSON.stringify(<TreeNode[]> children));
    //   // event.node.children = [];
    //   node.children = <TreeNode[]>children;
    // });
  }

  loadChildren(node: TreeNode) {
    if (node) {
      if (node.data.content.level) {
        // console.debug('Loading children of node: ' + JSON.stringify(event.node));
        this.loading = true;
        this.lipids = Array<TreeNode>(node);
        this.nodeClick(node);
        if (node.children && node.children.length > 0) {
          this.selectionPath.push(node);
          this.children = node.children;
          this.loading = false;
        } else {
          this.lipidNodeService.childrenFor(node, node['lipidLevel']).subscribe(children => {
            // console.debug('Received children: ' + JSON.stringify(<TreeNode[]> children));
            // event.node.children = [];
            node.children = <TreeNode[]>children;
            this.selectionPath.push(node);
            this.children = node.children;
            this.loading = false;
          });
        }
      } else {
        console.debug('level is undefined on node: ' + JSON.stringify(node));
      }
    }
  }
}
