Feature: grep by tag

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

  Scenario: output background step
    Given a feature at "a.feature":
    """
    # language: zh-CN
    @tag1
    功能: 功能名字
      背景: 背景
        假如存在1, "a"
      Rule: 规则名字
        背景: 背景
          假如存在2, "b"
        场景: 场景1
    """
    When grep "a.feature" and specify tag: "@tag1"
    Then output should be:
    """
    a.feature: ```
               # language: zh-CN
               @tag1
               功能: 功能名字

                 背景: 背景
                   假如存在1, "a"

                 Rule: 规则名字

                   背景: 背景
                     假如存在2, "b"

                   场景: 场景1
               ```
    """

  Scenario: output scenario step
    Given a feature at "a.feature":
    """
    # language: zh-CN
    @tag1
    功能: 功能名字
      场景: 场景1
        假如存在1, "a"
      Rule: 规则名字
        场景: 场景2
          假如存在2, "b"
    """
    When grep "a.feature" and specify tag: "@tag1"
    Then output should be:
    """
    a.feature: ```
               # language: zh-CN
               @tag1
               功能: 功能名字

                 场景: 场景1
                   假如存在1, "a"

                 Rule: 规则名字

                   场景: 场景2
                     假如存在2, "b"
               ```
    """

  Scenario: output step with table
    Given a feature at "a.feature":
    """
    # language: zh-CN
    @tag1
    功能: 功能名字
      场景: 场景1
        假如存在:
          |a|b|
          |12|34|
    """
    When grep "a.feature" and specify tag: "@tag1"
    Then output should be:
    """
    a.feature: ```
               # language: zh-CN
               @tag1
               功能: 功能名字

                 场景: 场景1
                   假如存在:
                     | a  | b  |
                     | 12 | 34 |
               ```
    """

  Scenario: output step with doc
    Given a feature at "a.feature":
    """
    # language: zh-CN
    @tag1
    功能: 功能名字
      场景: 场景1
        假如存在:
        \"\"\"
        hello
          world
        \"\"\"
    """
    When grep "a.feature" and specify tag: "@tag1"
    Then output should be:
    """
    a.feature: ```
               # language: zh-CN
               @tag1
               功能: 功能名字

                 场景: 场景1
                   假如存在:
                     \"\"\"
                     hello
                       world
                     \"\"\"
               ```
    """

  Scenario: output step with doc and doc type
    Given a feature at "a.feature":
    """
    # language: zh-CN
    @tag1
    功能: 功能名字
      场景: 场景1
        假如存在:
        \"\"\"type
        hello
          world
        \"\"\"
    """
    When grep "a.feature" and specify tag: "@tag1"
    Then output should be:
    """
    a.feature: ```
               # language: zh-CN
               @tag1
               功能: 功能名字

                 场景: 场景1
                   假如存在:
                     \"\"\" type
                     hello
                       world
                     \"\"\"
               ```
    """

  Scenario: output scenario outline
    Given a feature at "a.feature":
    """
    # language: zh-CN
    @tag1
    功能: 功能名字
      场景大纲: 场景<a>
        假如存在<b>:
        \"\"\"type<d>
        hello<c>
        \"\"\"
        例子:
          | a | b | c | d |
          | 1 | 2 | 3 | 40 |
    """
    When grep "a.feature" and specify tag: "@tag1"
    Then output should be:
    """
    a.feature: ```
               # language: zh-CN
               @tag1
               功能: 功能名字

                 场景大纲: 场景<a>
                   假如存在<b>:
                     \"\"\" type<d>
                     hello<c>
                     \"\"\"
                   例子:
                     | a | b | c | d  |
                     | 1 | 2 | 3 | 40 |
               ```
    """
