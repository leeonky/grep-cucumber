Feature: grep by tag
#  format
#  background in feature
#  step
#  table
#  doc
#  outline

  Scenario: output feature with correct format
    Given a feature at "a.feature":
    """
    # language: zh-CN
    @tag1  @tag2
    功能: 功能名字
    第1行描述
    第2行描述
      背景: 背景
      第1行背景
      第2行背景
      Rule: 规则名字
      第1行规则
      第2行规则
        背景: 背景2
        第1行背景2
        第2行背景2
        场景: 场景1
          第1行场景
          第2行场景
    """
    When grep "a.feature" and specify tag: "@tag1"
    Then output should be:
    """
    a.feature: ```
               # language: zh-CN
               @tag1 @tag2
               功能: 功能名字
                 第1行描述
                 第2行描述

                 背景: 背景
                   第1行背景
                   第2行背景

                 Rule: 规则名字
                   第1行规则
                   第2行规则

                   背景: 背景2
                     第1行背景2
                     第2行背景2

                   场景: 场景1
                     第1行场景
                     第2行场景
               ```
    """

