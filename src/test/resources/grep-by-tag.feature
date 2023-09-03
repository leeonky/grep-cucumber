Feature: grep by tag

  Scenario: output all feature without tag
    Given a feature at "a.feature":
    """
    # language: zh-CN
    功能: 报警界面
    """
    When grep "a.feature"
    Then output should be:
    """
    = {
      'a.feature'.string: ```
                          # language: zh-CN
                          功能: 报警界面
                          ```
    }
    """